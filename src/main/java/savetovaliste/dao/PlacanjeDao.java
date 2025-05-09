package savetovaliste.dao;

import savetovaliste.model.Dugovanje;
import savetovaliste.model.Placanje;
import savetovaliste.model.Seansa;
import savetovaliste.model.Test;
import savetovaliste.utility.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlacanjeDao {

    public PlacanjeDao() {
    }


    public List<Placanje> vratiUplateZaKlijentaIPsihoterapeuta(int klijentId, int psihoterapeutId) {
        List<Placanje> uplate = new ArrayList<>();

        String sql = """
        SELECT 
            p.placanje_id,
            p.datum_uplate,
            p.iznos,
            p.svrha,
            p.napomena,
            p.rata,
            p.provizija_5_posto,
            p.seansa_id,
            p.valuta_id,
            v.naziv AS valuta_naziv,
            p.nacin_id,
            np.naziv AS nacin_naziv
        FROM placanje p
        JOIN seansa s ON p.seansa_id = s.seansa_id
        JOIN valuta v ON p.valuta_id = v.valuta_id
        JOIN nacin_placanja np ON p.nacin_id = np.nacin_id
        WHERE s.klijent_id = ? AND s.psihoterapeut_kandidat_id = ?
        ORDER BY p.datum_uplate DESC
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, klijentId);
            stmt.setInt(2, psihoterapeutId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Placanje p = new Placanje();
                    p.setPlacanjeId(rs.getInt("placanje_id"));
                    p.setDatumUplate(rs.getDate("datum_uplate"));
                    p.setIznos(rs.getBigDecimal("iznos"));
                    p.setSvrha(rs.getString("svrha"));
                    p.setNapomena(rs.getString("napomena"));
                    p.setRata(rs.getObject("rata") != null ? rs.getInt("rata") : null);
                    p.setProvizija5Posto(rs.getBoolean("provizija_5_posto"));
                    p.setSeansaId(rs.getInt("seansa_id"));
                    p.setValutaId(rs.getString("valuta_id"));
                    p.setValutaNaziv(rs.getString("valuta_naziv"));
                    p.setNacinPlacanjaId(rs.getString("nacin_id"));
                    p.setNacinPlacanjaNaziv(rs.getString("nacin_naziv"));

                    uplate.add(p);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return uplate;
    }

    public boolean daLiKlijentDuguje(int klijentId, int psihoterapeutId) {
        String sql = """
        SELECT COUNT(*) AS broj
        FROM seansa s
        LEFT JOIN placanje p ON s.seansa_id = p.seansa_id
        LEFT JOIN rezultat_testiranja rt ON s.seansa_id = rt.seansa_id
        LEFT JOIN test t ON rt.test_id = t.test_id
        LEFT JOIN cena_seanse cs ON s.cena_id = cs.cena_id
        WHERE s.klijent_id = ?
          AND s.psihoterapeut_kandidat_id = ?
          AND s.odrzana = true
          AND (
            NOT EXISTS (
              SELECT 1 FROM placanje p2 WHERE p2.seansa_id = s.seansa_id
            )
            OR
            (EXISTS (
              SELECT 1 FROM placanje p3 WHERE p3.seansa_id = s.seansa_id AND p3.svrha = 'SEANSA - PRVA RATA'
            ) AND NOT EXISTS (
              SELECT 1 FROM placanje p4 WHERE p4.seansa_id = s.seansa_id AND p4.svrha = 'SEANSA - DRUGA RATA'
            ) AND DATEDIFF(CURDATE(), s.dan) > 30)
            OR
            (EXISTS (
              SELECT 1 FROM rezultat_testiranja rt2 WHERE rt2.seansa_id = s.seansa_id
            ) AND NOT EXISTS (
              SELECT 1 FROM placanje p5 WHERE p5.seansa_id = s.seansa_id AND p5.svrha = 'TEST'
            ))
          )
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, klijentId);
            stmt.setInt(2, psihoterapeutId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("broj") > 0;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public List<Dugovanje> vratiDugovanjaZaKlijentaIpsiho(int klijentId, int psihoterapeutId) {
        List<Dugovanje> lista = new ArrayList<>();

        String sql = """
        SELECT 
            s.seansa_id,
            s.dan,
            cs.cena,
            (SELECT COUNT(*) FROM placanje p1 WHERE p1.seansa_id = s.seansa_id AND p1.svrha = 'SEANSA - PRVA RATA') AS ima_prvu,
            (SELECT COUNT(*) FROM placanje p2 WHERE p2.seansa_id = s.seansa_id AND p2.svrha = 'SEANSA - DRUGA RATA') AS ima_drugu,
            (SELECT COUNT(*) FROM placanje p3 WHERE p3.seansa_id = s.seansa_id AND p3.svrha = 'SEANSA - CELA') AS ima_celu,
            (SELECT p2.datum_uplate FROM placanje p2 WHERE p2.seansa_id = s.seansa_id AND p2.svrha = 'SEANSA - DRUGA RATA' LIMIT 1) AS datum_druge_rate,
            (SELECT p.valuta_id FROM placanje p WHERE p.seansa_id = s.seansa_id LIMIT 1) AS valuta_id
        FROM seansa s
        LEFT JOIN cena_seanse cs ON s.cena_id = cs.cena_id
        WHERE s.klijent_id = ?
        AND s.psihoterapeut_kandidat_id = ?
        AND s.odrzana = true
    """;


        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, klijentId);
            stmt.setInt(2, psihoterapeutId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int seansaId = rs.getInt("seansa_id");
                    java.sql.Date datum = rs.getDate("dan");
                    java.math.BigDecimal cena = rs.getBigDecimal("cena");
                    String valuta = rs.getString("valuta_id") != null ? rs.getString("valuta_id") : "RSD";
                    Date datumDrugeRate = rs.getDate("datum_druge_rate");

                    int imaPrvu = rs.getInt("ima_prvu");
                    int imaDrugu = rs.getInt("ima_drugu");
                    int imaCelu = rs.getInt("ima_celu");

                    String opis = "";
                    java.math.BigDecimal iznos = new java.math.BigDecimal("0");

                    if (imaCelu == 0) {
                        if (imaPrvu == 0 && imaDrugu == 0) {
                            opis += "Seansa nije plaćena. ";
                            iznos = iznos.add(cena);
                        } else if (imaPrvu == 1 && imaDrugu == 0 && datum.toLocalDate().plusDays(30).isBefore(java.time.LocalDate.now())) {
                            opis += "Druga rata nije plaćena u roku. ";
                            iznos = iznos.add(cena.divide(new java.math.BigDecimal("2")));
                        } else if (imaDrugu == 1 && datumDrugeRate != null && datumDrugeRate.toLocalDate().isAfter(datum.toLocalDate().plusDays(30))) {
                            opis += "Druga rata je plaćena sa zakašnjenjem. ";
                        }
                    }

                    // proveri testove posebno i dodaj ako ih ima
                    String testSql = """
                    SELECT t.naziv, t.cena
                    FROM rezultat_testiranja rt
                    JOIN test t ON rt.test_id = t.test_id
                    WHERE rt.seansa_id = ? AND NOT EXISTS (
                        SELECT 1 FROM placanje p WHERE p.seansa_id = rt.seansa_id AND p.napomena = t.naziv AND p.svrha = 'TEST'
                    )
                """;

                    try (PreparedStatement testStmt = conn.prepareStatement(testSql)) {
                        testStmt.setInt(1, seansaId);

                        try (ResultSet trs = testStmt.executeQuery()) {
                            while (trs.next()) {
                                String testNaziv = trs.getString("naziv");
                                java.math.BigDecimal testCena = trs.getBigDecimal("cena");
                                opis += "Test nije plaćen: " + testNaziv + ". ";
                                iznos = iznos.add(testCena);
                            }
                        }
                    }

                    if (!opis.isEmpty()) {
                        Dugovanje d = new Dugovanje();
                        d.setSeansaId(seansaId);
                        d.setDatum(datum);
                        d.setOpis(opis.trim());
                        d.setIznos(iznos);
                        d.setValuta(valuta);
                        lista.add(d);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    public void unesiPlacanjeZaCeluSeansu(Seansa seansa, List<Test> testovi) {

       // procedura za unos uplate seanse
        String sql = "CALL unesi_placanja_za_celu_seansu(?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, seansa.getSeansaId());
            stmt.setString(2, "RSD");
            stmt.setString(3, "G");

            stmt.execute();

            // ubacivanje plaćanja za testove
            for (Test test : testovi) {                             // za svaki test unosio placanje
                CallableStatement testStmt = conn.prepareCall("CALL unesi_placanje_testa(?, ?, ?, ?, ?)");
                testStmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
                testStmt.setBigDecimal(2, BigDecimal.valueOf(test.getCena()));
                testStmt.setString(3, "TEST");
                testStmt.setString(4, test.getNaziv());
                testStmt.setInt(5, seansa.getSeansaId());
                testStmt.execute();
                testStmt.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

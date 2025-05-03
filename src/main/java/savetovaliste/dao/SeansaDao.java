package savetovaliste.dao;

import savetovaliste.model.Klijent;
import savetovaliste.model.Seansa;
import savetovaliste.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SeansaDao {

    public List<Klijent> vratiNoveKLijente(int psihoterapeutId) {

        List<Klijent> lista = new ArrayList<Klijent>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT k.*, p.pol AS pol_naziv " +
                             "FROM klijent k " +
                             "JOIN seansa s ON k.klijent_id = s.klijent_id " +
                             "JOIN pol p ON k.pol_pol_id = p.pol_id " +
                             "WHERE s.psihoterapeut_kandidat_id = ? " +
                             "GROUP BY k.klijent_id " +
                             "HAVING COUNT(s.seansa_id) = 1")
        ) {
            stmt.setInt(1, psihoterapeutId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Klijent k = new Klijent();
                k.setId(rs.getInt("klijent_id"));
                k.setIme(rs.getString("ime"));
                k.setPrezime(rs.getString("prezime"));
                k.setDatumRodjenja(rs.getDate("datum_rodjenja"));
                k.setTelefon(rs.getString("telefon"));
                k.setEmail(rs.getString("email"));
                k.setOpis(rs.getString("opis_problema"));
                k.setPrviPut(rs.getBoolean("prvi_put"));
                k.setPol(rs.getString("pol_naziv"));
                lista.add(k);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public List<Klijent> vratiSveKlijente(int psihoterapeutId) {
        List<Klijent> lista = new ArrayList<>();

        String sql = "SELECT DISTINCT k.*, p.pol AS pol_naziv " +
                "FROM klijent k " +
                "JOIN seansa s ON k.klijent_id = s.klijent_id " +
                "JOIN pol p ON k.pol_pol_id = p.pol_id " +
                "WHERE s.psihoterapeut_kandidat_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, psihoterapeutId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Klijent k = new Klijent();
                k.setId(rs.getInt("klijent_id"));
                k.setIme(rs.getString("ime"));
                k.setPrezime(rs.getString("prezime"));
                k.setDatumRodjenja(rs.getDate("datum_rodjenja"));
                k.setTelefon(rs.getString("telefon"));
                k.setEmail(rs.getString("email"));
                k.setOpis(rs.getString("opis_problema"));
                k.setPrviPut(rs.getBoolean("prvi_put"));
                k.setPol(rs.getString("pol_naziv"));
                lista.add(k);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }


    public boolean unesiSeansuProcedura(Seansa seansa) {
        String sql = "{CALL dodaj_seansu(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, new java.sql.Date(seansa.getDatum().getTime()));
            stmt.setTime(2, seansa.getVreme()); // mora biti u formatu "HH:mm:ss"
            stmt.setString(3, seansa.getBeleske());
            stmt.setBigDecimal(4, seansa.getTrajanje());
            stmt.setBoolean(5, seansa.isPlacanje());
            stmt.setBoolean(6, seansa.isZlocin());
            stmt.setBoolean(7, seansa.isNaSudu());
            stmt.setInt(8, seansa.getKlijentId());
            stmt.setInt(9, seansa.getCenaId());
            stmt.setInt(10, seansa.getPihoterapeutId());

            stmt.execute();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public List<Seansa> vratiOdrzaneSeanse(int psihoterapeutId) {
        List<Seansa> lista = new ArrayList<>();

        String sql = """
        SELECT s.*, k.ime, k.prezime
        FROM seansa s
        JOIN klijent k ON s.klijent_id = k.klijent_id
        WHERE s.psihoterapeut_kandidat_id = ?
        AND (s.dan < CURDATE() OR (s.dan = CURDATE() AND s.vreme < CURTIME()))
        ORDER BY s.dan DESC, s.vreme DESC
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, psihoterapeutId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // napravi Seansa objekat i dodaj u listu (dodaću ti klasu ako treba)
                Seansa s = new Seansa(
                        rs.getInt("seansa_id"),
                        rs.getDate("dan"),
                        rs.getTime("vreme"),
                        rs.getString("beleske"),
                        rs.getBigDecimal("trajanje"),
                        rs.getBoolean("placanje"),
                        rs.getBoolean("zlocin"),
                        rs.getBoolean("na_sudu"),
                        rs.getInt("klijent_id"),
                        rs.getString("ime"),
                        rs.getString("prezime"),
                        rs.getInt("cena_id"),
                        rs.getInt("psihoterapeut_kandidat_id")
                );
                lista.add(s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
}

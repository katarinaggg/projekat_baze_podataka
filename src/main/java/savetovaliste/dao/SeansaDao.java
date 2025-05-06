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
        List<Klijent> lista = new ArrayList<>();
        String sql = "SELECT k.*, p.pol AS pol_naziv " +
                "FROM klijent k " +
                "JOIN seansa s ON k.klijent_id = s.klijent_id " +
                "JOIN pol p ON k.pol_pol_id = p.pol_id " +
                "WHERE s.psihoterapeut_kandidat_id = ? " +
                "GROUP BY k.klijent_id " +
                "HAVING COUNT(s.seansa_id) = 1";

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
        String sql = "{CALL dodaj_seansu(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, new java.sql.Date(seansa.getDatum().getTime()));
            stmt.setTime(2, seansa.getVreme()); // format: HH:mm:ss
            stmt.setBigDecimal(3, seansa.getTrajanje());
            stmt.setBoolean(4, seansa.getPlacanje());
            stmt.setInt(5, seansa.getKlijentId());
            stmt.setInt(6, seansa.getPsihoterapeutId());
            stmt.setString(7, seansa.getBeleske());
            stmt.setBoolean(8, false); // zločin: podrazumevano false
            stmt.setBoolean(9, false); // na sudu: podrazumevano false

            if (seansa.getCenaId() != null) {
                stmt.setInt(10, seansa.getCenaId());
            } else {
                stmt.setNull(10, Types.INTEGER);
            }

            stmt.setBoolean(11, seansa.isOdrzana());

            stmt.execute();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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

    /*
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
*/

    public List<Seansa> vratiOdrzaneSeanse(int psihoterapeutId) {
        List<Seansa> lista = new ArrayList<>();

        String sql = """
        SELECT s.*, k.ime, k.prezime
        FROM seansa s
        JOIN klijent k ON s.klijent_id = k.klijent_id
        WHERE s.psihoterapeut_kandidat_id = ?
        AND s.odrzana = TRUE
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

    public boolean zakaziSeansu(int klijentId, int psihoterapeutId, Date datum, Time vreme) {
        String sql = "{CALL zakazi_seansu(?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, new java.sql.Date(datum.getTime()));
            stmt.setTime(2, vreme);
            stmt.setInt(3, klijentId);
            stmt.setInt(4, psihoterapeutId);

            return stmt.executeUpdate() == 1;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public List<Seansa> vratiBuduceSeanse(int psihoterapeutId) {
        List<Seansa> lista = new ArrayList<>();
        String sql = "SELECT s.seansa_id, s.dan, s.vreme, s.klijent_id, k.ime, k.prezime " +
                "FROM seansa s " +
                "JOIN klijent k ON s.klijent_id = k.klijent_id " +
                "WHERE s.psihoterapeut_kandidat_id = ? AND s.odrzana = FALSE AND s.dan >= CURDATE() " +
                "ORDER BY s.dan, s.vreme";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, psihoterapeutId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Seansa s = new Seansa();
                s.setSeansaId(rs.getInt("seansa_id"));
                s.setDatum(rs.getDate("dan"));
                s.setVreme(rs.getTime("vreme"));
                s.setKlijentId(rs.getInt("klijent_id"));
                s.setKlijentIme(rs.getString("ime"));
                s.setKlijentPrezime(rs.getString("prezime"));
                lista.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean postojiZakazanaSeansa(int psihoterapeutId, Date datum, Time vreme) {
        String sql = "SELECT COUNT(*) FROM seansa " +
                "WHERE psihoterapeut_kandidat_id = ? AND dan = ? AND vreme = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, psihoterapeutId);
            stmt.setDate(2, new java.sql.Date(datum.getTime()));
            stmt.setTime(3, vreme);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Seansa nadjiSeansuPoId(int seansaId) {
        String sql = "SELECT s.*, k.ime, k.prezime " +
                "FROM seansa s " +
                "JOIN klijent k ON s.klijent_id = k.klijent_id " +
                "WHERE s.seansa_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, seansaId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Seansa s = new Seansa();
                s.setSeansaId(seansaId);
                s.setDatum(rs.getDate("dan"));
                s.setVreme(rs.getTime("vreme"));
                s.setKlijentId(rs.getInt("klijent_id"));
                s.setKlijentIme(rs.getString("ime"));
                s.setKlijentPrezime(rs.getString("prezime"));
                s.setPsihoterapeutId(rs.getInt("psihoterapeut_kandidat_id"));
                return s;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public boolean oznaciSeansuKaoOdrzanu(Seansa s) {
        String sql = "{CALL oznaci_seansu_kao_odrzanu(?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, s.getSeansaId());
            stmt.setString(2, s.getBeleske());
            stmt.setBigDecimal(3, s.getTrajanje());
            stmt.setBoolean(4, s.getPlacanje());
            stmt.setBoolean(5, s.getZlocin());
            stmt.setBoolean(6, s.getNaSudu());
            stmt.setInt(7, s.getCenaId());

            stmt.execute();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
package savetovaliste.dao;

import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PsihoKandDao {

    public List<PsihoterapeutKandidat> getAllKandidati() {
        List<PsihoterapeutKandidat> lista = new ArrayList<>();
//        String upit = "SELECT " +
//                "pk.psihoterapeut_id, " +
//                "pk.jmbg, " +
//                "pk.ime, " +
//                "pk.prezime, " +
//                "pk.datum_rodjenja, " +
//                "pk.telefon, " +
//                "pk.prebivaliste, " +
//                "pk.email, " +
//                "f.naziv AS fakultet_naziv, " +
//                "s.stepen_id, " +         // <<<<< dodaj ovde da ne prijavljuje grešku
//                "s.stepen_studija " +
//                "FROM psihoterapeut_kandidat pk " +
//                "JOIN fakultet f ON pk.fakultet_id = f.fakultet_id " +
//                "JOIN stepen s ON pk.stepen_id = s.stepen_id " +
//                "JOIN sertifikat sert ON pk.psihoterapeut_id = sert.psihoterapeut_kandidat_id";
//
//        String upit = "SELECT " +
//                "pk.psihoterapeut_id, " +
//                "pk.jmbg, " +
//                "pk.ime, " +
//                "pk.prezime, " +
//                "pk.datum_rodjenja, " +
//                "pk.telefon, " +
//                "pk.prebivaliste, " +
//                "pk.email, " +
//                "f.naziv AS fakultet_naziv, " +
//                "s.stepen_id, " +
//                "s.stepen_studija " +
//                "FROM psihoterapeut_kandidat pk " +
//                "JOIN fakultet f ON pk.fakultet_id = f.fakultet_id " +
//                "JOIN univerzitet u ON f.univerzitet_id = u.univerzitet_id " +
//                "JOIN usmerenje us ON u.usmerenje_id = us.usmerenje_id " +
//                "JOIN stepen s ON pk.stepen_id = s.stepen_id " +
//                "JOIN sertifikat sert ON pk.psihoterapeut_id = sert.psihoterapeut_kandidat_id " +
//                "WHERE us.naziv = 'Humanističke nauke'";

        String upit= "SELECT " +
                "pk.psihoterapeut_id, " +
                "pk.jmbg, " +
                "pk.ime, " +
                "pk.prezime, " +
                "pk.datum_rodjenja, " +
                "pk.telefon, " +
                "pk.prebivaliste, " +
                "pk.email, " +
                "f.naziv AS fakultet_naziv, " +
                "s.stepen_id, " +
                "s.stepen_studija " +
                "FROM psihoterapeut_kandidat pk " +
                "JOIN fakultet f ON pk.fakultet_id = f.fakultet_id " +
                "JOIN univerzitet u ON f.univerzitet_id = u.univerzitet_id " +
                "JOIN usmerenje us ON u.usmerenje_id = us.usmerenje_id " +
                "JOIN stepen s ON pk.stepen_id = s.stepen_id " +
/*bez left*/    "JOIN sertifikat sert ON pk.psihoterapeut_id = sert.psihoterapeut_kandidat_id " +
                //"WHERE us.naziv = 'Humanističke nauke' " +
                "WHERE sert.psihoterapeut_kandidat_id IS NOT NULL " +
                "ORDER BY pk.prezime ASC";



        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(upit)) {

            while (rs.next()) {
                lista.add(new PsihoterapeutKandidat(
                        rs.getInt("psihoterapeut_id"),
                        rs.getLong("jmbg"),
                        rs.getString("ime"),
                        rs.getString("prezime"),
                        rs.getDate("datum_rodjenja"),
                        rs.getString("telefon"),
                        rs.getString("prebivaliste"),
                        rs.getString("email"),
                        rs.getString("fakultet_naziv"),
                        rs.getString("stepen_studija")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean daLiJeRegistrovan(String jmbg, String email) {
        String query = "SELECT registrovan FROM psihoterapeut_kandidat WHERE jmbg = ? OR email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, jmbg);
            stmt.setString(2, email);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("registrovan");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean registrujKandidata(PsihoterapeutKandidat k) {
        String sql = "{CALL registruj_kandidata(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setLong(1, k.getJmbg());
            cs.setString(2, k.getIme());
            cs.setString(3, k.getPrezime());
            cs.setDate(4, Date.valueOf(k.getDatumRodjenja()));
            cs.setString(5, k.getTelefon());
            cs.setString(6, k.getPrebivaliste());
            cs.setString(7, k.getEmail());
            cs.setString(8, k.getLozinka());
            cs.setInt(9, k.getFakultetId());
            cs.setString(10, k.getStepenId());

            cs.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void postaviRegistrovan(String jmbg) {
        String sql = "{CALL postavi_registrovan(?)}";
        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            cs.setString(1, jmbg);
            cs.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public PsihoterapeutKandidat login(String email, String lozinka) {

        // trazimo u bazi osobu sa istim mejlom i lozinkom i koji mora da bude registrovan
        // spajamo tabelu sa sertifikatima jer osoba mora da ima sert tj da bude psihoterapeut
        String query = "SELECT k.psihoterapeut_id, k.jmbg, k.ime, k.prezime, k.datum_rodjenja, k.telefon, " +
                       "k.prebivaliste, k.email, k.lozinka, k.registrovan, k.fakultet_id, k.stepen_id " +
                       "FROM psihoterapeut_kandidat k " +
                       "JOIN sertifikat s ON k.psihoterapeut_id = s.psihoterapeut_kandidat_id " +
                       "WHERE k.email = ? AND k.lozinka = ? AND k.registrovan = TRUE;";

        try (Connection conn = DatabaseConnection.getConnection()){

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, lozinka);
            ResultSet rs = ps.executeQuery();

            // ako smo nasli osobu
            if(rs.next()) {
                // pravimo objakat koji popunjavamo informacijama iz baze
                PsihoterapeutKandidat k = new PsihoterapeutKandidat();
                k.setJmbg(rs.getLong("jmbg"));
                k.setIme(rs.getString("ime"));
                k.setPrezime(rs.getString("prezime"));
                k.setDatumRodjenja(rs.getDate("datum_rodjenja"));
                k.setTelefon(rs.getString("telefon"));
                k.setPrebivaliste(rs.getString("prebivaliste"));
                k.setEmail(rs.getString("email"));
                k.setLozinka(rs.getString("lozinka"));
                k.setRegistrovan(rs.getBoolean("registrovan"));
                k.setFakultetId(rs.getInt("fakultet_id"));
                k.setStepenId(rs.getString("stepen_id"));
                return k;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // ako ne uspe vratice null
        return null;
    }

    public void obrisiKandidata(String jmbg) {
        String sql = "{CALL obrisi_kandidata(?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, jmbg);
            cs.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}

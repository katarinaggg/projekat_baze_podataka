package savetovaliste.repository;

import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PsihoKandRep {
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
                "LEFT JOIN sertifikat sert ON pk.psihoterapeut_id = sert.psihoterapeut_kandidat_id " +
                "WHERE us.naziv = 'Humanističke nauke' " +
                "OR sert.psihoterapeut_kandidat_id IS NOT NULL " +
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
                        rs.getString("stepen_id")
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
        String sql = "INSERT INTO psihoterapeut_kandidat (jmbg, ime, prezime, datum_rodjenja, telefon, prebivaliste, email, lozinka, fakultet_id, stepen_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, k.getJmbg());
            ps.setString(2, k.getIme());
            ps.setString(3, k.getPrezime());
            ps.setDate(4, Date.valueOf(k.getDatumRodjenja()));
            ps.setString(5, k.getTelefon());
            ps.setString(6, k.getPrebivaliste());
            ps.setString(7, k.getEmail());
            ps.setString(8, k.getLozinka());
            ps.setInt(9, k.getFakultetId());
            ps.setString(10, k.getStepenId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void postaviRegistrovan(String jmbg) {
        String query = "UPDATE psihoterapeut_kandidat SET registrovan = TRUE WHERE jmbg = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, jmbg);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

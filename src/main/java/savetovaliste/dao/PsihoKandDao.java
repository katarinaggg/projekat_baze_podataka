package savetovaliste.dao;

import savetovaliste.model.PsihoterapeutKandidat;
import savetovaliste.utility.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PsihoKandDao {
//    public List<PsihoterapeutKandidat> getAllKandidati() {
//        List<PsihoterapeutKandidat> lista = new ArrayList<>();
//
//        String upit= "SELECT " +
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
///*bez left*/    "JOIN sertifikat sert ON pk.psihoterapeut_id = sert.psihoterapeut_kandidat_id " +
//                //"WHERE us.naziv = 'Humanističke nauke' " +
//                "WHERE sert.psihoterapeut_kandidat_id IS NOT NULL " +
//                "ORDER BY pk.prezime ASC";
//
//
//
//        try (Connection conn = DatabaseConnection.getConnection();
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(upit)) {
//
//            while (rs.next()) {
//                lista.add(new PsihoterapeutKandidat(
//                        rs.getInt("psihoterapeut_id"),
//                        rs.getLong("jmbg"),
//                        rs.getString("ime"),
//                        rs.getString("prezime"),
//                        rs.getDate("datum_rodjenja"),
//                        rs.getString("telefon"),
//                        rs.getString("prebivaliste"),
//                        rs.getString("email"),
//                        rs.getString("fakultet_naziv"),
//                        rs.getString("stepen_studija")
//                ));
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return lista;
//    }


    public List<PsihoterapeutKandidat> getAllKandidati() {
        List<PsihoterapeutKandidat> lista = new ArrayList<>();

        String sql = "SELECT " +
                "pk.psihoterapeut_id, " +
                "pk.jmbg, " +
                "pk.ime, " +
                "pk.prezime, " +
                "pk.datum_rodjenja, " +
                "pk.telefon, " +
                "pk.prebivaliste, " +
                "pk.email, " +
                "f.naziv AS fakultet_naziv, " +
                "s.stepen_studija " +
                "FROM psihoterapeut_kandidat pk " +
                "JOIN fakultet f ON pk.fakultet_id = f.fakultet_id " +
                "JOIN stepen s ON pk.stepen_id = s.stepen_id " +
                "WHERE pk.registrovan = TRUE";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                PsihoterapeutKandidat k = new PsihoterapeutKandidat();
                k.setPsihoterapeutId(rs.getInt("psihoterapeut_id"));
                k.setJmbg(rs.getLong("jmbg"));
                k.setIme(rs.getString("ime"));
                k.setPrezime(rs.getString("prezime"));
                k.setDatumRodjenja(rs.getDate("datum_rodjenja"));
                k.setTelefon(rs.getString("telefon"));
                k.setPrebivaliste(rs.getString("prebivaliste"));
                k.setEmail(rs.getString("email"));
                k.setFakultetNaziv(rs.getString("fakultet_naziv"));
                k.setStepenNaziv(rs.getString("stepen_studija"));

                // System.out.println("[" + k.getPsihoterapeutId() + "] " + k.getIme() + " - " + k.getStepenNaziv());

                lista.add(k);
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
            cs.setDate(4, (k.getDatumRodjenja()));
            cs.setString(5, k.getTelefon());
            cs.setString(6, k.getPrebivaliste());
            cs.setString(7, k.getEmail());
            cs.setString(8, k.getLozinka());
            cs.setInt(9, k.getFakultetId());
            cs.setString(10, k.getStepenId());

            cs.execute();
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

    public boolean dodajSertifikat(long jmbg, int oblastId, java.sql.Date datum) {
        String sql = "CALL dodaj_sertifikat(?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, jmbg);
            ps.setInt(2, oblastId);
            ps.setDate(3, datum);
            ps.execute();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public int getIdPoJmbg(long jmbg) {
        String sql = "SELECT psihoterapeut_id FROM psihoterapeut_kandidat WHERE jmbg = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, jmbg);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("psihoterapeut_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }



//    public PsihoterapeutKandidat login(String email, String lozinka) {
//
//        // trazimo u bazi osobu sa istim mejlom i lozinkom i koji mora da bude registrovan
//        // spajamo tabelu sa sertifikatima jer osoba mora da ima sert tj da bude psihoterapeut
//        String query = "SELECT k.psihoterapeut_id, k.jmbg, k.ime, k.prezime, k.datum_rodjenja, k.telefon, " +
//                       "k.prebivaliste, k.email, k.lozinka, k.registrovan, k.fakultet_id, k.stepen_id " +
//                       "FROM psihoterapeut_kandidat k " +
//                       "JOIN sertifikat s ON k.psihoterapeut_id = s.psihoterapeut_kandidat_id " +
//                       "WHERE k.email = ? AND k.lozinka = ? AND k.registrovan = TRUE;";
//
//        try (Connection conn = DatabaseConnection.getConnection()){
//
//            PreparedStatement ps = conn.prepareStatement(query);
//            ps.setString(1, email);
//            ps.setString(2, lozinka);
//            ResultSet rs = ps.executeQuery();
//
//            // ako smo nasli osobu
//            if(rs.next()) {
//                // pravimo objakat koji popunjavamo informacijama iz baze
//                PsihoterapeutKandidat k = new PsihoterapeutKandidat();
//                k.setJmbg(rs.getLong("jmbg"));
//                k.setIme(rs.getString("ime"));
//                k.setPrezime(rs.getString("prezime"));
//                k.setDatumRodjenja(rs.getDate("datum_rodjenja"));
//                k.setTelefon(rs.getString("telefon"));
//                k.setPrebivaliste(rs.getString("prebivaliste"));
//                k.setEmail(rs.getString("email"));
//                k.setLozinka(rs.getString("lozinka"));
//                k.setRegistrovan(rs.getBoolean("registrovan"));
//                k.setFakultetId(rs.getInt("fakultet_id"));
//                k.setStepenId(rs.getString("stepen_id"));
//                return k;
//            }
//
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//        // ako ne uspe vratice null
//        return null;
//    }

    public PsihoterapeutKandidat login(String email, String lozinka) {
        String query = "SELECT k.psihoterapeut_id, k.jmbg, k.ime, k.prezime, k.datum_rodjenja, k.telefon, " +
                "k.prebivaliste, k.email, k.lozinka, k.registrovan, " +
                "f.naziv AS fakultet_naziv, s.stepen_studija, " +
                "se.datum_sertifikacije, o.naziv AS oblast_naziv, " +
                "sv.ime AS supervizor_ime, sv.prezime AS supervizor_prezime " +
                "FROM psihoterapeut_kandidat k " +
                "LEFT JOIN fakultet f ON k.fakultet_id = f.fakultet_id " +
                "LEFT JOIN stepen s ON k.stepen_id = s.stepen_id " +
                "LEFT JOIN sertifikat se ON k.psihoterapeut_id = se.psihoterapeut_kandidat_id " +
                "LEFT JOIN oblast_psihoterapije o ON se.sert_oblast_id = o.sert_oblast_id " +
                "LEFT JOIN psihoterapeut_kandidat sv ON k.psihoterapeut_id = sv.psihoterapeut_id " +
                "WHERE k.email = ? AND k.lozinka = ? AND k.registrovan = TRUE";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, email);
            ps.setString(2, lozinka);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PsihoterapeutKandidat k = new PsihoterapeutKandidat();

                k.setPsihoterapeutId(rs.getInt("psihoterapeut_id"));
                k.setJmbg(rs.getLong("jmbg"));
                k.setIme(rs.getString("ime"));
                k.setPrezime(rs.getString("prezime"));
                k.setDatumRodjenja(rs.getDate("datum_rodjenja"));
                k.setTelefon(rs.getString("telefon"));
                k.setPrebivaliste(rs.getString("prebivaliste"));
                k.setEmail(rs.getString("email"));
                k.setLozinka(rs.getString("lozinka"));
                k.setRegistrovan(rs.getBoolean("registrovan"));

                k.setFakultetNaziv(rs.getString("fakultet_naziv"));
                k.setStepenNaziv(rs.getString("stepen_studija"));

                k.setDatumSertifikacije(rs.getDate("datum_sertifikacije"));
                k.setOblastNaziv(rs.getString("oblast_naziv"));
                k.setSupervizorIme(rs.getString("supervizor_ime"));
                k.setSupervizorPrezime(rs.getString("supervizor_prezime"));

                return k;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void obrisiKandidataPoEmailu(String email) {
        String sql = "{CALL obrisi_kandidata_po_emailu(?)}";

        try (Connection conn = DatabaseConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {

            cs.setString(1, email);
            cs.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public PsihoterapeutKandidat getKandidatAkoImaZavrsenuSuperviziju(String jmbg) {
        PsihoterapeutKandidat kandidat = null;

        String sql = """
        SELECT pk.*
        FROM psihoterapeut_kandidat pk
        JOIN rad_pod_supervizijom rps ON pk.psihoterapeut_id = rps.kandidat_id
        WHERE pk.jmbg = ? AND rps.datum_zavrsetka IS NOT NULL
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, jmbg);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                kandidat = new PsihoterapeutKandidat();
                kandidat.setPsihoterapeutId(rs.getInt("psihoterapeut_id"));
                kandidat.setIme(rs.getString("ime"));
                kandidat.setPrezime(rs.getString("prezime"));
                kandidat.setJmbg(rs.getLong("jmbg"));
                kandidat.setTelefon(rs.getString("telefon"));
                kandidat.setEmail(rs.getString("email"));
                kandidat.setPrebivaliste(rs.getString("prebivaliste"));
                kandidat.setDatumRodjenja(rs.getDate("datum_rodjenja"));
                kandidat.setFakultetId(rs.getInt("fakultet_id"));
                kandidat.setStepenId(rs.getString("stepen_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return kandidat;
    }

    public PsihoterapeutKandidat nadjiPoEmailu(String email) {
        String sql = "SELECT * FROM psihoterapeut_kandidat WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                PsihoterapeutKandidat k = new PsihoterapeutKandidat();
                k.setPsihoterapeutId(rs.getInt("psihoterapeut_id"));
                k.setIme(rs.getString("ime"));
                k.setPrezime(rs.getString("prezime"));
                k.setDatumRodjenja(rs.getDate("datum_rodjenja"));
                k.setTelefon(rs.getString("telefon"));
                k.setPrebivaliste(rs.getString("prebivaliste"));
                k.setEmail(rs.getString("email"));
                k.setLozinka(rs.getString("lozinka"));
                k.setFakultetId(rs.getInt("fakultet_id"));
                k.setStepenId(rs.getString("stepen_id"));
                // Dodaj i ostale ako treba
                return k;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}

package savetovaliste.model;

import savetovaliste.utility.DatabaseConnection;

import java.sql.*;

public class PsihoterapeutKandidat {
    private int psihoterapeutId;
    private long jmbg;
    private String ime;
    private String prezime;
    private Date datumRodjenja;
    private String telefon;
    private String prebivaliste;
    private String email;
    private String lozinka;
    private int fakultetId;
    private String fakultetNaziv;
    private String stepenId;
    private String stepenNaziv;
    private boolean registrovan;

    private Date datumSertifikacije;
    private String oblastNaziv;
    private String supervizorIme;
    private String supervizorPrezime;


    public PsihoterapeutKandidat() {}

    public PsihoterapeutKandidat(int psihoterapeutId, long jmbg, String ime, String prezime, Date datumRodjenja,
                                 String telefon, String prebivaliste, String email, String lozinka,
                                 String fakultetNaziv, String stepenId) {
        this.psihoterapeutId = psihoterapeutId;
        this.jmbg = jmbg;
        this.ime = ime;
        this.prezime = prezime;
        this.datumRodjenja = datumRodjenja;
        this.telefon = telefon;
        this.prebivaliste = prebivaliste;
        this.email = email;
        this.lozinka = lozinka;
        this.fakultetNaziv = fakultetNaziv;
        this.stepenId = stepenId;
    }

    public PsihoterapeutKandidat(int psihoterapeutId, long jmbg, String ime, String prezime, Date datumRodjenja, String telefon, String prebivaliste, String email, String fakultetNaziv, String stepenNaziv) {
        this.psihoterapeutId = psihoterapeutId;
        this.jmbg = jmbg;
        this.ime = ime;
        this.prezime = prezime;
        this.datumRodjenja = datumRodjenja;
        this.telefon = telefon;
        this.prebivaliste = prebivaliste;
        this.email = email;
        this.fakultetNaziv = fakultetNaziv;
        this.stepenNaziv = stepenNaziv;
    }


    // Getteri i setteri

    public int getPsihoterapeutId() { return psihoterapeutId; }
    public void setPsihoterapeutId(int id) { this.psihoterapeutId = id; }

    public long getJmbg() { return jmbg; }
    public void setJmbg(long jmbg) { this.jmbg = jmbg; }

    public String getIme() { return ime; }
    public void setIme(String ime) { this.ime = ime; }

    public String getPrezime() { return prezime; }
    public void setPrezime(String prezime) { this.prezime = prezime; }

    public Date getDatumRodjenja() {
        return datumRodjenja;
    }

    public void setDatumRodjenja(Date datum) { this.datumRodjenja = datum; }

    public String getTelefon() { return telefon; }
    public void setTelefon(String tel) { this.telefon = tel; }

    public String getPrebivaliste() { return prebivaliste; }
    public void setPrebivaliste(String preb) { this.prebivaliste = preb; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLozinka() {
        return lozinka;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public int getFakultetId() { return fakultetId; }
    public void setFakultetId(int fakultetId) { this.fakultetId = fakultetId; }

    public String getFakultetNaziv() {
        return fakultetNaziv;
    }

    public void setFakultetNaziv(String fakultetNaziv) {
        this.fakultetNaziv = fakultetNaziv;
    }

    public boolean isRegistrovan() {
        return registrovan;
    }

    public void setRegistrovan(boolean registrovan) {
        this.registrovan = registrovan;
    }

    public String getStepenNaziv() {
        return stepenNaziv;
    }

    public void setStepenNaziv(String stepenNaziv) {
        this.stepenNaziv = stepenNaziv;
    }

    public String getStepenId() { return stepenId; }
    public void setStepenId(String stepenId) { this.stepenId = stepenId; }

    public Date getDatumSertifikacije() {
        return datumSertifikacije;
    }

    public void setDatumSertifikacije(Date datumSertifikacije) {
        this.datumSertifikacije = datumSertifikacije;
    }

    public String getOblastNaziv() {
        return oblastNaziv;
    }

    public void setOblastNaziv(String oblastNaziv) {
        this.oblastNaziv = oblastNaziv;
    }

    public String getSupervizorIme() {
        return supervizorIme;
    }

    public void setSupervizorIme(String supervizorIme) {
        this.supervizorIme = supervizorIme;
    }

    public String getSupervizorPrezime() {
        return supervizorPrezime;
    }

    public void setSupervizorPrezime(String supervizorPrezime) {
        this.supervizorPrezime = supervizorPrezime;
    }

    @Override
    public String toString() {
        return ime + " " + prezime + " (" + email + ")";
    }

    public PsihoterapeutKandidat getPsihoterapeutProfil(int psihoterapeutId) {
        String sql = "SELECT \n" +
                "    pk.psihoterapeut_id,\n" +
                "    pk.jmbg,\n" +
                "    pk.ime,\n" +
                "    pk.prezime,\n" +
                "    pk.datum_rodjenja,\n" +
                "    pk.telefon,\n" +
                "    pk.prebivaliste,\n" +
                "    pk.email,\n" +
                "    pk.lozinka,\n" +
                "    f.naziv AS fakultet_naziv,\n" +
                "    s.stepen_studija,\n" +
                "    sert.datum_sertifikacije,\n" +
                "    o.naziv AS oblast_naziv,\n" +
                "    sv.ime AS supervizor_ime,\n" +
                "    sv.prezime AS supervizor_prezime\n" +
                "FROM psihoterapeut_kandidat pk\n" +
                "JOIN fakultet f ON pk.fakultet_id = f.fakultet_id\n" +
                "JOIN stepen s ON pk.stepen_id = s.stepen_id\n" +
                "JOIN sertifikat sert ON pk.psihoterapeut_id = sert.psihoterapeut_kandidat_id\n" +
                "JOIN oblast_psihoterapije o ON sert.sert_oblast_id = o.sert_oblast_id\n" +
                "JOIN supervizor sv ON pk.psihoterapeut_id = sv.psihoterapeut_id\n" +
                "WHERE pk.psihoterapeut_id = ?"; // gore navedeni SQL

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, psihoterapeutId);
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

}

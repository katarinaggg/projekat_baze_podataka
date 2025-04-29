package savetovaliste.model;

import java.sql.Date;
import java.time.LocalDate;

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
    private boolean registrovan;

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

    public PsihoterapeutKandidat(int psihoterapeutId, long jmbg, String ime, String prezime, Date datumRodjenja, String telefon, String prebivaliste, String email, String fakultetNaziv, String stepenId) {
        this.psihoterapeutId = psihoterapeutId;
        this.jmbg = jmbg;
        this.ime = ime;
        this.prezime = prezime;
        this.datumRodjenja = datumRodjenja;
        this.telefon = telefon;
        this.prebivaliste = prebivaliste;
        this.email = email;
        this.fakultetNaziv = fakultetNaziv;
        this.stepenId = stepenId;
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

    public LocalDate getDatumRodjenja() { return datumRodjenja.toLocalDate(); }
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



    public String getStepenId() { return stepenId; }
    public void setStepenId(String stepenId) { this.stepenId = stepenId; }

    @Override
    public String toString() {
        return ime + " " + prezime + " (" + email + ")";
    }
}

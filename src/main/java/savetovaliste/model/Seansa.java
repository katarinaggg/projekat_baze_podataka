package savetovaliste.model;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

public class Seansa {

    private int seansaId;
    private Date datum;
    private Time vreme;
    private String beleske;
    private BigDecimal trajanje;
    private Boolean placanje;          // može biti NULL u bazi
    private Boolean zlocin;            // može biti NULL
    private Boolean naSudu;            // može biti NULL
    private int klijentId;
    private String klijentIme;
    private String klijentPrezime;
    private Integer cenaId;
    private int psihoterapeutId;
    private boolean odrzana;


    public Seansa() {}

    public Seansa(Date datum, Time vreme, String beleske, BigDecimal trajanje,
                  Boolean placanje, boolean odrzana, Boolean zlocin, Boolean naSudu,
                  int klijentId, Integer cenaId, int psihoterapeutId) {
        this.datum = datum;
        this.vreme = vreme;
        this.beleske = beleske;
        this.trajanje = trajanje;
        this.placanje = placanje;
        this.odrzana = odrzana;
        this.zlocin = zlocin;
        this.naSudu = naSudu;
        this.klijentId = klijentId;
        this.cenaId = cenaId;
        this.psihoterapeutId = psihoterapeutId;
    }

    public Seansa(int seansaId, Date datum, Time vreme, String beleske, BigDecimal trajanje,
                  Boolean placanje, Boolean zlocin, Boolean naSudu, int klijentId,
                  String klijentIme, String klijentPrezime, Integer cenaId, int psihoterapeutId) {
        this.seansaId = seansaId;
        this.datum = datum;
        this.vreme = vreme;
        this.beleske = beleske;
        this.trajanje = trajanje;
        this.placanje = placanje;
        this.zlocin = zlocin;
        this.naSudu = naSudu;
        this.klijentId = klijentId;
        this.klijentIme = klijentIme;
        this.klijentPrezime = klijentPrezime;
        this.cenaId = cenaId;
        this.psihoterapeutId = psihoterapeutId;
    }

    public int getSeansaId() {
        return seansaId;
    }

    public void setSeansaId(int seansaId) {
        this.seansaId = seansaId;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public Time getVreme() {
        return vreme;
    }

    public void setVreme(Time vreme) {
        this.vreme = vreme;
    }

    public String getBeleske() {
        return beleske;
    }

    public void setBeleske(String beleske) {
        this.beleske = beleske;
    }

    public BigDecimal getTrajanje() {
        return trajanje;
    }

    public void setTrajanje(BigDecimal trajanje) {
        this.trajanje = trajanje;
    }

    public Boolean getPlacanje() {
        return placanje;
    }

    public void setPlacanje(Boolean placanje) {
        this.placanje = placanje;
    }

    public Boolean getZlocin() {
        return zlocin;
    }

    public void setZlocin(Boolean zlocin) {
        this.zlocin = zlocin;
    }

    public Boolean getNaSudu() {
        return naSudu;
    }

    public void setNaSudu(Boolean naSudu) {
        this.naSudu = naSudu;
    }

    public int getKlijentId() {
        return klijentId;
    }

    public void setKlijentId(int klijentId) {
        this.klijentId = klijentId;
    }

    public String getKlijentIme() {
        return klijentIme;
    }

    public void setKlijentIme(String klijentIme) {
        this.klijentIme = klijentIme;
    }

    public String getKlijentPrezime() {
        return klijentPrezime;
    }

    public void setKlijentPrezime(String klijentPrezime) {
        this.klijentPrezime = klijentPrezime;
    }

    public Integer getCenaId() {
        return cenaId;
    }

    public void setCenaId(Integer cenaId) {
        this.cenaId = cenaId;
    }

    public int getPsihoterapeutId() {
        return psihoterapeutId;
    }

    public void setPsihoterapeutId(int pihoterapeutId) {
        this.psihoterapeutId = pihoterapeutId;
    }

    public boolean isOdrzana() {
        return odrzana;
    }

    public void setOdrzana(boolean odrzana) {
        this.odrzana = odrzana;
    }

    public String getKlijentImePrezime() {
        return klijentIme + " " + klijentPrezime;
    }
}

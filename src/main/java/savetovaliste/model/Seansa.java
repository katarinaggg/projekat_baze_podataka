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
    private boolean placanje;
    private boolean zlocin;
    private boolean naSudu;
    private int klijentId;
    private String klijentIme;
    private String klijentPrezime;
    private int cenaId;
    private int pihoterapeutId;

    public Seansa(Date datum, Time vreme, String beleske, BigDecimal trajanje, boolean placanje, boolean zlocin, boolean naSudu, int klijentId, int cenaId, int pihoterapeutId) {
        this.datum = datum;
        this.vreme = vreme;
        this.beleske = beleske;
        this.trajanje = trajanje;
        this.placanje = placanje;
        this.zlocin = zlocin;
        this.naSudu = naSudu;
        this.klijentId = klijentId;
        this.cenaId = cenaId;
        this.pihoterapeutId = pihoterapeutId;
    }

    public Seansa(int seansaId, Date datum, Time vreme, String beleske, BigDecimal trajanje, boolean placanje,
                  boolean zlocin, boolean naSudu, int klijentId, String klijentIme, String klijentPrezime,
                  int cenaId, int pihoterapeutId) {
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
        this.pihoterapeutId = pihoterapeutId;
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

    public boolean isPlacanje() {
        return placanje;
    }

    public void setPlacanje(boolean placanje) {
        this.placanje = placanje;
    }

    public boolean isZlocin() {
        return zlocin;
    }

    public void setZlocin(boolean zlocin) {
        this.zlocin = zlocin;
    }

    public boolean isNaSudu() {
        return naSudu;
    }

    public void setNaSudu(boolean naSudu) {
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

    public int getCenaId() {
        return cenaId;
    }

    public void setCenaId(int cenaId) {
        this.cenaId = cenaId;
    }

    public int getPihoterapeutId() {
        return pihoterapeutId;
    }

    public void setPihoterapeutId(int pihoterapeutId) {
        this.pihoterapeutId = pihoterapeutId;
    }

    public String getKlijentImePrezime() {
        return klijentIme + " " + klijentPrezime;
    }
}

package savetovaliste.model;

import java.math.BigDecimal;
import java.sql.Date;

public class Dugovanje {

    private int seansaId;
    private Date datum;
    private BigDecimal iznos;  // ukupno i s testovima
    private String opis;
    private String valuta;

    public Dugovanje() {
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

    public BigDecimal getIznos() {
        return iznos;
    }

    public void setIznos(BigDecimal iznos) {
        this.iznos = iznos;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public String getValuta() {
        return valuta;
    }

    public void setValuta(String valuta) {
        this.valuta = valuta;
    }
}

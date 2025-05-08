package savetovaliste.model;

import java.math.BigDecimal;
import java.util.Date;

public class Placanje {

    private int placanjeId;
    private Date datumUplate;
    private BigDecimal iznos;

    private String svrha;

    private String napomena;
    private Integer rata;
    private boolean provizija5Posto;

    private int seansaId;
    private String valutaId;
    private String nacinPlacanjaId;

    private String nacinPlacanjaNaziv;
    private String valutaNaziv;

    public Placanje() {
    }

    public int getPlacanjeId() {
        return placanjeId;
    }

    public void setPlacanjeId(int placanjeId) {
        this.placanjeId = placanjeId;
    }

    public Date getDatumUplate() {
        return datumUplate;
    }

    public void setDatumUplate(Date datumUplate) {
        this.datumUplate = datumUplate;
    }

    public BigDecimal getIznos() {
        return iznos;
    }

    public void setIznos(BigDecimal iznos) {
        this.iznos = iznos;
    }

    public String getSvrha() {
        return svrha;
    }

    public void setSvrha(String svrha) {
        this.svrha = svrha;
    }

    public String getNapomena() {
        return napomena;
    }

    public void setNapomena(String napomena) {
        this.napomena = napomena;
    }

    public Integer getRata() {
        return rata;
    }

    public void setRata(Integer rata) {
        this.rata = rata;
    }

    public boolean isProvizija5Posto() {
        return provizija5Posto;
    }

    public void setProvizija5Posto(boolean provizija5Posto) {
        this.provizija5Posto = provizija5Posto;
    }

    public int getSeansaId() {
        return seansaId;
    }

    public void setSeansaId(int seansaId) {
        this.seansaId = seansaId;
    }

    public String getValutaId() {
        return valutaId;
    }

    public void setValutaId(String valutaId) {
        this.valutaId = valutaId;
    }

    public String getNacinPlacanjaId() {
        return nacinPlacanjaId;
    }

    public void setNacinPlacanjaId(String nacinPlacanjaId) {
        this.nacinPlacanjaId = nacinPlacanjaId;
    }

    public String getNacinPlacanjaNaziv() {
        return nacinPlacanjaNaziv;
    }

    public void setNacinPlacanjaNaziv(String nacinPlacanjaNaziv) {
        this.nacinPlacanjaNaziv = nacinPlacanjaNaziv;
    }

    public String getValutaNaziv() {
        return valutaNaziv;
    }

    public void setValutaNaziv(String valutaNaziv) {
        this.valutaNaziv = valutaNaziv;
    }
}

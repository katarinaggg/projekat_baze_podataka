package savetovaliste.model;

public class OblastPsihoterapije {
    private  int oblastPsihoteraoijeId;
    private String naziv;

    public int getOblastPsihoteraoijeId() {
        return oblastPsihoteraoijeId;
    }

    public void setOblastPsihoteraoijeId(int oblastPsihoteraoijeId) {
        this.oblastPsihoteraoijeId = oblastPsihoteraoijeId;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Override
    public String toString() {
        return naziv;
    }
}

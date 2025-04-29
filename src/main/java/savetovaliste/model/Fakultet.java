package savetovaliste.model;

public class Fakultet {
    private int fakultetId;
    private String naziv;

    public Fakultet(int fakultetId, String naziv) {
        this.fakultetId = fakultetId;
        this.naziv = naziv;
    }

    public Fakultet() {
    }

    public int getFakultetId() {
        return fakultetId;
    }

    public void setFakultetId(int fakultetId) {
        this.fakultetId = fakultetId;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    @Override
    public String toString() {
        return  naziv;
    }


}

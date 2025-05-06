package savetovaliste.model;

public class Pol {
    private String id;     // 'M' ili 'Z'
    private String naziv;  // 'Muški' ili 'Ženski'

    public Pol(String id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public String getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    @Override
    public String toString() {
        return naziv;
    }
}
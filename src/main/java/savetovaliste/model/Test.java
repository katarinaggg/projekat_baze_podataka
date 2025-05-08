package savetovaliste.model;

public class Test {

    private int testId;
    private String naziv;
    private double cena;
    private int testOblastId;

    public Test(int testId, String naziv, double cena, int testOblastId) {
        this.testId = testId;
        this.naziv = naziv;
        this.cena = cena;
        this.testOblastId = testOblastId;
    }

    public Test() {

    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }

    public int getTestOblastId() {
        return testOblastId;
    }

    public void setTestOblastId(int testOblastId) {
        this.testOblastId = testOblastId;
    }

    @Override
    public String toString() {
        return naziv;
    }
}

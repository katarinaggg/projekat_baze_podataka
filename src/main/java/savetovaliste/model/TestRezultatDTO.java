package savetovaliste.model;

import java.math.BigDecimal;

public class TestRezultatDTO {

    //test rezultat data transfer object  naziv testa + cenu + rezultat klijenta u jednoj tabeli
    //Oznaka DTO znači Data Transfer Object — to je obrazac (pattern) u programiranju koji služi za prenos podataka između slojeva aplikacije, posebno kada:
    //ne želiš da koristiš ceo entitet (model),
    //ili kada kombinuješ podatke iz više izvora (npr. više tabela).
    private String naziv;
    private BigDecimal cena;
    private String rezultat;

    public TestRezultatDTO(String naziv, BigDecimal cena, String rezultat) {
        this.naziv = naziv;
        this.cena = cena;
        this.rezultat = rezultat;
    }

    public String getNaziv() {
        return naziv;
    }

    public BigDecimal getCena() {
        return cena;
    }

    public String getRezultat() {
        return rezultat;
    }
}
package savetovaliste.model;

import java.util.Date;

public class Objava {
    private Date datumObjave;
    private String primalac;
    private String razlog;

    public Objava(Date datumObjave, String primalac, String razlog) {
        this.datumObjave = datumObjave;
        this.primalac = primalac;
        this.razlog = razlog;
    }

    public Date getDatumObjave() {
        return datumObjave;
    }

    public String getPrimalac() {
        return primalac;
    }

    public String getRazlog() {
        return razlog;
    }
}

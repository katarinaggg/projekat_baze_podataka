package savetovaliste.model;

import java.util.Date;

public class Objava {

    private int ObjavaId;
    private Date datumObjave;
    private String primalac;
    private String razlog;

    public Objava(Date datumObjave, String primalac, String razlog) {
        this.datumObjave = datumObjave;
        this.primalac = primalac;
        this.razlog = razlog;
    }

    public Objava() {
    }

    public int getObjavaId() {
        return ObjavaId;
    }

    public void setObjavaId(int objavaId) {
        ObjavaId = objavaId;
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

    public void setDatumObjave(Date datumObjave) {
        this.datumObjave = datumObjave;
    }

    public void setPrimalac(String primalac) {
        this.primalac = primalac;
    }

    public void setRazlog(String razlog) {
        this.razlog = razlog;
    }
}

package model;

public class Client {

    private int id;
    private String nume;
    private String adresa;
    private int varsta;

    public Client(int idClient, String nume, String adresa, int varsta) {
        super();
        this.id = idClient;
        this.nume = nume;
        this.adresa = adresa;
        this.varsta = varsta;
    }

    public Client(String nume, String adresa, int varsta) {
        super();
        this.nume = nume;
        this.adresa = adresa;
        this.varsta = varsta;
    }

    public Client()
    {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int idClient) {
        this.id = idClient;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public int getVarsta() {
        return varsta;
    }

    public void setVarsta(int varsta) {
        this.varsta = varsta;
    }

    @Override
    public String toString() {
        return "Client{" +
                "idClient=" + id +
                ", nume='" + nume + '\'' +
                ", adresa='" + adresa + '\'' +
                ", varsta=" + varsta +
                '}';
    }

}

package model;

public class Product {

    private int id;
    private String nume;
    private int stoc;
    private int pret;

    public Product(int idProdus, String nume, int stoc, int pret) {
        super();
        this.id = idProdus;
        this.nume = nume;
        this.stoc = stoc;
        this.pret = pret;
    }

    public Product(String nume, int stoc, int pret) {
        super();
        this.nume = nume;
        this.stoc = stoc;
        this.pret = pret;
    }

    public Product()
    {
        super();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public int getStoc() {
        return stoc;
    }

    public void setStoc(int stoc) {
        this.stoc = stoc;
    }

    public int getPret() {
        return pret;
    }

    public void setPret(int pret) {
        this.pret = pret;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", stoc=" + stoc +
                ", pret=" + pret +
                '}';
    }
}

package model;

public class Comanda {

    private int id;
    private int idClient;
    private int idProduct;
    private int cantitate;

    public Comanda(int id, int idClient, int idProduct, int cantitate) {
        super();
        this.id = id;
        this.idClient = idClient;
        this.idProduct = idProduct;
        this.cantitate = cantitate;
    }

    public Comanda(int idClient, int idProduct, int cantitate) {
        //this.id = id;
        super();
        this.idClient = idClient;
        this.idProduct = idProduct;
        this.cantitate = cantitate;
    }

    public Comanda()
    {
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdClient() {
        return idClient;
    }

    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", idClient=" + idClient +
                ", idProduct=" + idProduct +
                ", cantitate=" + cantitate +
                '}';
    }
}

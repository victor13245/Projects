package model;

public class BillVechi {


    private int id;
    private int idClient;
    private int idProduct;
    private int cantitate;
   // private int idBill;

    public BillVechi(int id, int idClient, int idProduct, int cantitate) {
        super();
        this.id = id;
        this.idClient = idClient;
        this.idProduct = idProduct;
        this.cantitate = cantitate;
    }

    public BillVechi(int idClient, int idProduct, int cantitate) {
        //this.id = id;
        super();
        this.idClient = idClient;
        this.idProduct = idProduct;
        this.cantitate = cantitate;
    }

    public BillVechi()
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

//    public int getIdBill() {
//        return idBill;
//    }
//
//    public void setIdBill(int idBill) {
//        this.idBill = idBill;
//    }

    @Override
    public String toString() {
        return "Bill{" +
                "id=" + id +
                ", idClient=" + idClient +
                ", idProduct=" + idProduct +
                ", cantitate=" + cantitate +
                '}';
    }

}

package model;

public class Clienti implements Comparable{

    private int id;
    private int arrivalTime;
    private int serviceTime;


    public Clienti(int id, int arrivalTime, int serviceTime) {
        super();
        this.id = id;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }


    public int getArrivalTime() {
        return arrivalTime;
    }


    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }


    public int getServiceTime() {
        return serviceTime;
    }


    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }


    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }



    @Override
    public String toString() {
        return "(" + id + ", " + (arrivalTime+1) + ", " + serviceTime + ")";
    }


    @Override
    public int compareTo(Object o) {
        // TODO Auto-generated method stub
        return Integer.compare(this.getArrivalTime(), ((Clienti)o).getArrivalTime());

    }





}


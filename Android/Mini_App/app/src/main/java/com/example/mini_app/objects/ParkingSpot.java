package com.example.mini_app.objects;

public class ParkingSpot {

    private String key;
    private String name;
    private double lat;
    private double lng;
    private boolean occupied;
    private int nrSpaces;

    public ParkingSpot(String key, String name, int nrSpaces, boolean occupied, double lng, double lat) {
        this.key = key;
        this.name = name;
        this.nrSpaces = nrSpaces;
        this.occupied = occupied;
        this.lng = lng;
        this.lat = lat;
    }

    public ParkingSpot() {

    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }

    public int getNrSpaces() {
        return nrSpaces;
    }

    public void setNrSpaces(int nrSpaces) {
        this.nrSpaces = nrSpaces;
    }
}

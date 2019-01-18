package com.example.damo.carcostmanager;

public class Cost {

    String idCost;
    String data;
    String cost;
    String quantity;
    String distance;

    public Cost(){

    }

    public Cost(String idCost, String data, String cost, String quantity, String distance) {
        this.idCost = idCost;
        this.data = data;
        this.cost = cost;
        this.quantity = quantity;
        this.distance = distance;
    }

    public String getIdCost() {
        return idCost;
    }

    public String getData() {
        return data;
    }

    public String getCost() {
        return cost;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getDistance() {
        return distance;
    }

    public void setIdCost(String idCost) {
        this.idCost = idCost;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}

package com.example.damo.carcostmanager;

public class Cost {

    String idCost;
    String data;
    float cost;
    float quantity;
    float distance;

    public Cost(){

    }

    public Cost(String idCost, String data, float cost, float quantity, float distance) {
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

    public float getCost() {
        return cost;
    }

    public float getQuantity() {
        return quantity;
    }

    public float getDistance() {
        return distance;
    }

    public void setIdCost(String idCost) {
        this.idCost = idCost;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }
}
package com.example.damo.carcostmanager;

public class CostInformation {

    public String data;
    public String cost;
    public String quantity;
    public String distance;

    public CostInformation(){

    }

    public CostInformation(String data, String cost, String quantity, String distance) {
        this.data = data;
        this.cost = cost;
        this.quantity = quantity;
        this.distance = distance;
    }
}

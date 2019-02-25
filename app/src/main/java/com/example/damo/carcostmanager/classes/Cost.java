package com.example.damo.carcostmanager.classes;

/**Klasa zawierajaca informacje na temat kosztu zerejestrowanego w programie
Informacje te sa przechowywane w chmurze a w aplikacji widoczne sa w Menu programu oraz w oknach pokazuajcyh historie w formie listy zarejestrowanych kosztow
 */

public class Cost {

    private String idCost;
    private String data;
    private float cost;
    private float quantity;
    private float distance;
    private String comments;

    public Cost(){

    }

    public Cost(String idCost, String data, float cost, float quantity, float distance, String comments) {
        this.idCost = idCost;
        this.data = data;
        this.cost = cost;
        this.quantity = quantity;
        this.distance = distance;
        this.comments = comments;
    }

    public Cost(String idCost, String data, float cost, float quantity, float distance) {
        this.idCost = idCost;
        this.data = data;
        this.cost = cost;
        this.quantity = quantity;
        this.distance = distance;
    }

    public Cost(String idCost, String data, float cost, float distance) {
        this.idCost = idCost;
        this.data = data;
        this.cost = cost;
        this.distance = distance;
    }

    public Cost(String data, float cost) {
        this.data = data;
        this.cost = cost;
    }

    public Cost(String idCost, String data, float cost, String comments) {
        this.idCost = idCost;
        this.data = data;
        this.cost = cost;
        this.comments = comments;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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

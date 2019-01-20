package com.example.damo.carcostmanager;

public class Car {

    String idCar;
    String brand;
    String model;
    String engine;

    public Car(){

    }

    public Car(String brand, String model, String engine) {
        this.brand = brand;
        this.model = model;
        this.engine = engine;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }
}

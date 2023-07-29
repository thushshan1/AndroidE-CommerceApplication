package com.example.b07group19.models;

import java.io.Serializable;

public class Product implements Serializable {
    public String name;
    public double price;
    public int quantity;

    public Product() {}

    public Product(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // order
    public Product(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        Product other = (Product) o;
        return this.name == other.name;

    }


}

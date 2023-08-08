package com.example.b07group19.models;

import com.example.b07group19.Model;
import com.example.b07group19.Products;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Store implements Serializable {
    public String owner; // email
//    public List<OrderDescription> pendingOrders;
//    public List<OrderDescription> completedOrders;


    public Store(String owner, String storeName, List<Products> products) {
        this.owner = owner;
        this.storeName = storeName;
        this.products = products;
    }



    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }


    public String storeName;

    public List<com.example.b07group19.Products> getProducts() {
        return products;
    }

    public void setProducts(List<com.example.b07group19.Products> products) {
        this.products = products;
    }

    public List<Products> products;
    public Store() {
        products = new ArrayList<Products>();
//        pendingOrders = new ArrayList<OrderDescription>();
//        completedOrders = new ArrayList<OrderDescription>();
    }

    public Store(String storeName) {
        this();
        this.storeName = storeName;
    }

    public String toString() {
        return storeName;
    }
}

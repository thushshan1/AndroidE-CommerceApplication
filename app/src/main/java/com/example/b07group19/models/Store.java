package com.example.b07group19.models;

import com.example.b07group19.Products;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Store implements Serializable {
    public String owner; // email
    public List<OrderDescription> pendingOrders;
    public List<OrderDescription> completedOrders;

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

    public List<com.example.b07group19.Products> getInventory() {
        return inventory;
    }

    public void setInventory(List<com.example.b07group19.Products> inventory) {
        this.inventory = inventory;
    }

    public String storeName;

    public List<Products> inventory;
    public Store() {
        inventory = new ArrayList<Products>();
        pendingOrders = new ArrayList<OrderDescription>();
        completedOrders = new ArrayList<OrderDescription>();
    }

    public Store(String storeName) {
        this();
        this.storeName = storeName;
    }

    public String toString() {
        return storeName;
    }
}

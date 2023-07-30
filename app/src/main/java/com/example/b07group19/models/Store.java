package com.example.b07group19.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Store implements Serializable {
    public String owner; // email
    public String storeName;

    public List<Product> inventory;

    public Store() {
        inventory = new ArrayList<Product>();
    }

    public Store(String storeName) {
        this();
        this.storeName = storeName;
    }

    public String toString() {
        return storeName;
    }
}

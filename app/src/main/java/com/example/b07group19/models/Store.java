package com.example.b07group19.models;

import com.example.b07group19.Products;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Store implements Serializable {
    public String owner; // email
    public String storeName;

    public List<Products> inventory;

    public Store() {
        inventory = new ArrayList<Products>();
    }

    public Store(String storeName) {
        this();
        this.storeName = storeName;
    }

    public String toString() {
        return storeName;
    }
}

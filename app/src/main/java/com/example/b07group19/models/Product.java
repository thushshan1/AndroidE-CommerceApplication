package com.example.b07group19.models;

public class Product {
    public String product;
    public double price;
    public String brand;
    public int quantity;
    public double length;
    public double width;
    public double height;
    public String turl;

    public Product() {

    }
    public Product(String product, double price, String brand, int quantity, double length, double width, double height, String turl) {
        this.product = product;
        this.price = price;
        this.brand = brand;
        this.quantity = quantity;
        this.length = length;
        this.width = width;
        this.height = height;
        this.turl = turl;
    }

}

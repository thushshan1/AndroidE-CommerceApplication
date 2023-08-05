package com.example.b07group19;

public class Products {

    String turl;
    String height;
    String length;
    String price;
    String product;
    String quantity;
    String width;

    String brand;

    Products()
    {

    }
    public Products(String turl, String height, String length, String price, String product, String quantity, String width, String brand) {
        this.turl = turl;
        this.height = height;
        this.length = length;
        this.price = price;
        this.product = product;
        this.quantity = quantity;
        this.width = width;
        this.brand = brand;
    }

    public String getTurl() {
        return turl;
    }

    public void setTurl(String turl) {
        this.turl = turl;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}

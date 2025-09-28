package com.example.ecommerceapp.models;

public class OrderItem {
    private String productId;
    private String productName;
    private String productBrand;
    private double price;
    private int quantity;
    private String imageUrl;

    public OrderItem() {
        // Default constructor for Firebase
    }

    public OrderItem(String productId, String productName, String productBrand, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.productBrand = productBrand;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters and Setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductBrand() {
        return productBrand;
    }

    public void setProductBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFormattedPrice() {
        return String.format("$%.2f", price);
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    public String getFormattedTotalPrice() {
        return String.format("$%.2f", getTotalPrice());
    }

    // Legacy methods for compatibility
    public String getCount() {
        return String.valueOf(quantity);
    }

    public void setCount(String count) {
        this.quantity = Integer.parseInt(count);
    }

    public String getPurl() {
        return imageUrl;
    }

    public void setPurl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBrand() {
        return productBrand;
    }

    public void setBrand(String productBrand) {
        this.productBrand = productBrand;
    }

    public void setPrice(String price) {
        this.price = Double.parseDouble(price);
    }
}
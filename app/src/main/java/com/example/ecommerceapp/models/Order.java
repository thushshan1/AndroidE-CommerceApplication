package com.example.ecommerceapp.models;

import java.util.List;

public class Order {
    private String orderId;
    private String orderDate;
    private String itemsPreview;
    private double total;
    private String customerId;
    private String storeId;
    
    // Public fields for compatibility with existing code
    public String userID;
    public String createDate;
    public String status;
    public List<OrderItem> items;
    public String storeName;
    
    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public Order() {
        // Default constructor for Firebase
    }

    public Order(String orderId, String orderDate, String storeName, String itemsPreview, double total, String status) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.storeName = storeName;
        this.itemsPreview = itemsPreview;
        this.total = total;
        this.status = status;
    }

    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    // Legacy method for compatibility
    public String getOrderID() {
        return orderId;
    }

    public void setOrderID(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getItemsPreview() {
        return itemsPreview;
    }

    public void setItemsPreview(String itemsPreview) {
        this.itemsPreview = itemsPreview;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getFormattedTotal() {
        return String.format("$%.2f", total);
    }

    public String getStatusDisplayName() {
        switch (status.toLowerCase()) {
            case "pending":
                return "Pending";
            case "confirmed":
                return "Confirmed";
            case "shipped":
                return "Shipped";
            case "delivered":
            case "completed":
                return "Completed";
            case "cancelled":
                return "Cancelled";
            default:
                return "Unknown";
        }
    }

    // Legacy methods for compatibility
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
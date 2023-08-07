package com.example.b07group19.models;

import com.example.b07group19.StoreOrdersActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Serializable {
    public final static String DATE_FORMAT = "EEEE, dd MMM yyyy";
    public final static String TIME_FORMAT = "hh:mm a";
    public final static String DATETIME_FORMAT = DATE_FORMAT + ", " + TIME_FORMAT;

    public Order() {
        items = new ArrayList<OrderItem>();
    }



    public static String orderID;
    public String userID;
    public String status;

    public String storeName;
    public List<OrderItem> items;
    public String createDate;

    public Order(String storeName, String userID){
        this();
        this.storeName = storeName;
        this.userID = userID;
        this.status = "pending";
    }

    public Order(StoreOrdersActivity storeOrdersActivity, int activity_store_orders, List<Order> pendingOrders) {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public static String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> cartlist) {
        this.items = cartlist;
    }

    public void addItem(OrderItem orderItem){
        items.add(orderItem);
    }


}
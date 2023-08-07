package com.example.b07group19.models;

import java.io.Serializable;

public class OrderDescription implements Serializable {
    private String customerName;
    private String createdDate;
    private String orderID;

    public OrderDescription() {}

    public OrderDescription(String customerName, String createdDate, String orderID){
        this.customerName = customerName;
        this.createdDate = createdDate;
        this.orderID = orderID;
    }

    public String getCustomerName(){
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate){
        this.createdDate = createdDate;
    }

    public String getOrderID(){
        return orderID;
    }

    public void setOrderID(String orderID){
        this.orderID = orderID;
    }
}

package com.example.b07group19.models;

import java.io.Serializable;

public class OrderDescription implements Serializable {
    public String orderID;
    public String customerName;
    public String createdDate;
    public int numItems;

    public OrderDescription(){}

    public OrderDescription(String orderID, String customerName, String createdDate){
        this.orderID = orderID;
        this.customerName = customerName;
        this.createdDate = createdDate;
    }
}

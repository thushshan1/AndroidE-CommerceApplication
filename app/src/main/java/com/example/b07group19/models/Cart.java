package com.example.b07group19.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Cart {


    private double total;
    private Date createDate;
    private List<Order> orderList;

    public Map<String, Order> getOrderMap() {
        return orderMap;
    }

    public void setOrderMap(Map<String, Order> orderMap) {
        this.orderMap = orderMap;
    }

    //to make sure a sotre and only have one list in orderList
    private Map<String, Order> orderMap;
    private String customerID;

    public Cart() {
        orderList = new ArrayList<Order>();
        orderMap = new HashMap<String, Order>();
    }
    public void clear()
    {
        orderList.clear();
        orderMap.clear();
    }
    public boolean isEmpty()
    {
        if (orderList.size()==0)
            return true;
        return false;
    }
    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }


}

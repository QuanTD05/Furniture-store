package com.example.datn.model;

import java.util.List;

public class OrderModel {
    private String id;
    private String customerName;
    private String orderDate;
    private double totalAmount;
    private String status;
    private List<ProductInOrder> items;

    public OrderModel() {
    }

    public OrderModel(String id, String customerName, String orderDate, double totalAmount, String status, List<ProductInOrder> items) {
        this.id = id;
        this.customerName = customerName;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ProductInOrder> getItems() {
        return items;
    }

    public void setItems(List<ProductInOrder> items) {
        this.items = items;
    }
}

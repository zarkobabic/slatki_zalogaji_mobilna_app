package com.example.slatkizalogajimobilnaaplikacija.models;

import java.util.List;

public class Order {
    private int idOrder;
    private int idUser;
    private List<OrderItem> items;
    private String status;

    // Constructor
    public Order(int idOrder, int idUser, List<OrderItem> items, String status) {
        this.idOrder = idOrder;
        this.idUser = idUser;
        this.items = items;
        this.status = status;
    }

    public Order() {
    }

    // Getters and Setters
    public int getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
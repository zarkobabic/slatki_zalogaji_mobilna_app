package com.example.slatkizalogajimobilnaaplikacija.models;

public class OrderItem {
    private int idProduct;
    private String price;
    private int quantity;
    private String title;

    public OrderItem(int idProduct, String price, int quantity, String title) {
        this.idProduct = idProduct;
        this.price = price;
        this.quantity = quantity;
        this.title = title;
    }

    public OrderItem() {
    }

    public int getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
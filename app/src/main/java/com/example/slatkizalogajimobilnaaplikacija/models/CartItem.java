package com.example.slatkizalogajimobilnaaplikacija.models;

import java.io.Serializable;

public class CartItem implements Serializable {
    Integer idInCart;
    Integer idProduct;
    String productPrice;
    Integer productQuantity;
    String productTitle;

    public CartItem(Integer idInCart, Integer idProduct, String productPrice, Integer productQuantity, String productTitle) {
        this.idInCart = idInCart;
        this.idProduct = idProduct;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productTitle = productTitle;
    }

    public CartItem() {
    }

    public Integer getIdInCart() {
        return idInCart;
    }

    public void setIdInCart(Integer idInCart) {
        this.idInCart = idInCart;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }
}

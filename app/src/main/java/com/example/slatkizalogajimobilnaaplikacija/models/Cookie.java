package com.example.slatkizalogajimobilnaaplikacija.models;

import org.w3c.dom.Comment;

import java.util.List;

public class Cookie {
    private Integer idProduct;
    private String title;
    private String description;
    private String image;
    private String price;
    private List<String> composition;
    private List<Comment> comments;

    public Cookie() {
    }

    public Cookie(String title, String description, String image, String price, List<String> composition, List<Comment> comments, Integer idProduct) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
        this.composition = composition;
        this.comments = comments;
        this.idProduct = idProduct;
    }


    public Cookie(String title, String image) {
        this.title = title;
        this.image = image;
    }

    public Integer getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(Integer idProduct) {
        this.idProduct = idProduct;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public List<String> getComposition() {
        return composition;
    }

    public void setComposition(List<String> composition) {
        this.composition = composition;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
}

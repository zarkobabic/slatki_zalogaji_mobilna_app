package com.example.slatkizalogajimobilnaaplikacija.models;

public class PromotionModel {
    private int idPromotion;
    private String image;
    private String description;
    private String title;

    public PromotionModel() {
        // Default constructor required for calls to DataSnapshot.getValue(Promotion.class)
    }

    public PromotionModel(int idPromotion, String img, String text, String title) {
        this.idPromotion = idPromotion;
        this.image = img;
        this.description = text;
        this.title = title;
    }

    public int getIdPromotion() {
        return idPromotion;
    }

    public void setIdPromotion(int idPromotion) {
        this.idPromotion = idPromotion;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

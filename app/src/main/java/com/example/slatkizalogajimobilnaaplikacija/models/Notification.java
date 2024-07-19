package com.example.slatkizalogajimobilnaaplikacija.models;

import java.util.List;

public class Notification {
    private Integer idNotification;
    private Integer idUser;
    private Boolean isSuccess;
    private List<String> items;
    private List<Integer> quantities;


    public Notification(Integer idNotification, Integer idUser, Boolean isSuccess, List<String> items, List<Integer> quantities) {
        this.idNotification = idNotification;
        this.idUser = idUser;
        this.isSuccess = isSuccess;
        this.items = items;
        this.quantities = quantities;
    }

    public Notification() {
    }

    public Integer getIdNotification() {
        return idNotification;
    }

    public void setIdNotification(Integer idNotification) {
        this.idNotification = idNotification;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public void setIdUser(Integer idUser) {
        this.idUser = idUser;
    }

    public Boolean getSuccess() {
        return isSuccess;
    }

    public void setSuccess(Boolean success) {
        isSuccess = success;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public List<Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(List<Integer> quantities) {
        this.quantities = quantities;
    }
}

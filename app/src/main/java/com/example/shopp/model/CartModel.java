package com.example.shopp.model;

import java.util.List;

public class CartModel {
    private int status;
    private String message;
    private List<CartItem> result;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<CartItem> getResult() {
        return result;
    }

    public void setResult(List<CartItem> result) {
        this.result = result;
    }
}

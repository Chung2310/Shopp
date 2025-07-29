package com.example.shopp.model;

import java.util.List;

public class ReviewModel {
    private int status;
    private String message;
    private List<Review> result;

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

    public List<Review> getResult() {
        return result;
    }

    public void setResult(List<Review> result) {
        this.result = result;
    }
}

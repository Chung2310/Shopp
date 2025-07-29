package com.example.shopp.model;

import java.util.List;

public class ChechReviewedModel {
    private int status;
    private String message;
    private List<Long> result;

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

    public List<Long> getResult() {
        return result;
    }

    public void setResult(List<Long> result) {
        this.result = result;
    }
}

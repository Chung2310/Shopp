package com.example.shopp.model;

import java.util.List;

public class BookModel {
    private int status;
    private String message;
    private List<Book> result;

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

    public List<Book> getResult() {
        return result;
    }

    public void setResult(List<Book> result) {
        this.result = result;
    }
}

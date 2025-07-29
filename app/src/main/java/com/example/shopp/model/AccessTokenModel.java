package com.example.shopp.model;

public class AccessTokenModel {
    private int status;
    private String message;
    private RefreshTokenRequest result ;

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

    public RefreshTokenRequest getResult() {
        return result;
    }

    public void setResult(RefreshTokenRequest result) {
        this.result = result;
    }
}

package com.example.shopp.model;

public class AccessTokenModel {
    private int status;
    private String message;
    private RefreshTokenRequest refreshTokenRequest ;

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
        return refreshTokenRequest;
    }

    public void setResult(RefreshTokenRequest result) {
        this.refreshTokenRequest = result;
    }
}

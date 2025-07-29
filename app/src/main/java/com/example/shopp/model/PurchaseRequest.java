package com.example.shopp.model;

import java.util.List;

public class PurchaseRequest {
    private Long userId;
    private String address;
    private String phone;
    private String description;
    private List<OrderItem> items;

    public PurchaseRequest(Long userId, String address, String phone, String description, List<OrderItem> items) {
        this.userId = userId;
        this.address = address;
        this.phone = phone;
        this.description = description;
        this.items = items;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }
    public static class OrderItem{
        private Long bookId;
        private int quantity;

        public OrderItem(Long bookId, int quantity) {
            this.bookId = bookId;
            this.quantity = quantity;
        }

        public Long getBookId() {
            return bookId;
        }

        public void setBookId(Long bookId) {
            this.bookId = bookId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}

package com.example.shopp.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderDetail implements Serializable {
    private Long id;
    private Book bookDTO;
    private int quantity;
    private BigDecimal unitPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Book getBook() {
        return bookDTO;
    }

    public void setBook(Book book) {
        this.bookDTO = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}

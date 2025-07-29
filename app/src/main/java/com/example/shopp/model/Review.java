package com.example.shopp.model;

public class Review {
    private Long id;
    private User userDTO;
    private Book bookDTO;
    private int rating;
    private String comments;
    private String createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public User getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(User userDTO) {
        this.userDTO = userDTO;
    }

    public Book getBookDTO() {
        return bookDTO;
    }

    public void setBookDTO(Book bookDTO) {
        this.bookDTO = bookDTO;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}

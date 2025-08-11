package com.example.shopp.model;

import java.io.Serializable;

public class Contact implements Serializable {
    private Long id;
    private User userDTO;
    private User userContactDTO;
    private String lastMessage;
    private String lastTime;

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

    public User getUserContactDTO() {
        return userContactDTO;
    }

    public void setUserContactDTO(User userContactDTO) {
        this.userContactDTO = userContactDTO;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }
}

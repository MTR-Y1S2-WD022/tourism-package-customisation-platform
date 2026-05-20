package com.tourismplatform.model;

import java.time.LocalDateTime;

public class User extends Person {
    private int userId;
    private String phoneNumber;
    private String address;

    public User() {}

    public User(int userId, String fullName, String email, String password,
                String phoneNumber, String address, LocalDateTime createdAt) {
        super(fullName, email, password, createdAt);
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public User(int userId, String fullName, String email, String password,
                String phoneNumber, String address, String profileImage,
                LocalDateTime createdAt) {

        super(fullName, email, password, createdAt);
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
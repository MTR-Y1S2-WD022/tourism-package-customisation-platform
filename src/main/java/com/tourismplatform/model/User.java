package com.tourismplatform.model;

import java.time.LocalDateTime;

public class User extends Person {

    private int userId;
    private String phoneNumber;
    private String address;
    private String profileImage;

    public User() {
    }

    public User(int userId, String fullName, String email, String password,
                String phoneNumber, String address, String profileImage,
                String status, LocalDateTime createdAt) {
        super(fullName, email, password, status, createdAt);
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.profileImage = profileImage;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
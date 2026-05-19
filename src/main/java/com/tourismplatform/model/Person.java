package com.tourismplatform.model;

import java.time.LocalDateTime;

//dto
public class Person {

    protected String fullName;
    protected String email;
    protected String password;
    protected LocalDateTime createdAt;

    public Person() {
    }

    public Person(String fullName, String email, String password, LocalDateTime createdAt) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDashboardPath() {
        return "/home";
    }

    public String validate() {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "Full name is required.";
        }

        if (email == null || email.trim().isEmpty()) {
            return "Email is required.";
        }

        if (!email.contains("@")) {
            return "Please enter a valid email address.";
        }

        if (password == null || password.trim().isEmpty()) {
            return "Password is required.";
        }

        if (password.length() < 8) {
            return "Password must have at least 8 characters.";
        }

        return null;
    }


}
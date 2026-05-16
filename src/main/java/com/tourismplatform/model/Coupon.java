package com.tourismplatform.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public abstract class Coupon {

    private int couponId;
    private String couponCode;
    private String discountType;
    private double discountValue;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private String status;
    private Integer createdByAdminId;
    private LocalDateTime createdAt;

    public static final String ACTIVE = "ACTIVE";
    public static final String EXPIRED = "EXPIRED";

    public Coupon() {
    }

    public Coupon(int couponId, String couponCode, String discountType, double discountValue,
                  LocalDate issueDate, LocalDate expiryDate, String status,
                  Integer createdByAdminId, LocalDateTime createdAt) {
        this.couponId = couponId;
        this.couponCode = couponCode;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
        this.status = status;
        this.createdByAdminId = createdByAdminId;
        this.createdAt = createdAt;
    }

    public int getCouponId() {
        return couponId;
    }

    public void setCouponId(int couponId) {
        this.couponId = couponId;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getCreatedByAdminId() {
        return createdByAdminId;
    }

    public void setCreatedByAdminId(Integer createdByAdminId) {
        this.createdByAdminId = createdByAdminId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public abstract double calculateDiscount(double amount);
}


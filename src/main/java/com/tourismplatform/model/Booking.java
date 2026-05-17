package com.tourismplatform.model;

public abstract class Booking {

    protected double subtotalAmount;
    protected double discountAmount;

    public void setSubtotalAmount(double subtotalAmount) {
        this.subtotalAmount = subtotalAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public abstract double calculateTotal();
}
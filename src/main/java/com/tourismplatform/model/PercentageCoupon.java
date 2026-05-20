package com.tourismplatform.model;

public class PercentageCoupon extends Coupon {

    @Override
    public double calculateDiscount(double amount) {

        return amount * (getDiscountValue() / 100);
    }
}


package com.tourismplatform.model;

public class FixedCoupon extends Coupon {

    @Override
    public double calculateDiscount(double amount) {

        return getDiscountValue();
    }
}

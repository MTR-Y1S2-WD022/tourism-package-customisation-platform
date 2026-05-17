package com.tourismplatform.model;

public class CouponBooking extends Booking {

    @Override
    public double calculateTotal() {
        return subtotalAmount - discountAmount;
    }
}

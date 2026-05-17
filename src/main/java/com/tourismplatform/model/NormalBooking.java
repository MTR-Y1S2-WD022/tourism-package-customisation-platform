package com.tourismplatform.model;

public class NormalBooking extends Booking {

    @Override
    public double calculateTotal() {
        return subtotalAmount;
    }
}

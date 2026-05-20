package com.tourismplatform.model;

public class BookingDestination {

    private int id;
    private int bookingId;
    private int destinationId;

    public BookingDestination() {
    }

    public BookingDestination(int id, int bookingId, int destinationId) {
        this.id = id;
        this.bookingId = bookingId;
        this.destinationId = destinationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }
}

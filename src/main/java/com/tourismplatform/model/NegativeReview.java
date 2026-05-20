package com.tourismplatform.model;

public class NegativeReview extends ReviewType {

    @Override
    public String getRatingCategory() {
        return "Poor Review";
    }
}
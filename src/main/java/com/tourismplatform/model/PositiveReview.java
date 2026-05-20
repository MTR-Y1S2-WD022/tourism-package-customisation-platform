package com.tourismplatform.model;

public class PositiveReview extends ReviewType {

    @Override
    public String getRatingCategory() {
        return "Excellent Review";
    }
}
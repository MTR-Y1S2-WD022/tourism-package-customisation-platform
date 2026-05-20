package com.tourismplatform.model;

public class PackageWithoutDestinations  extends TourPackage {

    @Override
    public String getDestinationStatus() {
        return "No Destinations Assigned";
    }
}


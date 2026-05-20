package com.tourismplatform.model;


public class PackageDestination {

    private int id;
    private int packageId;
    private int destinationId;

    public PackageDestination() {
    }

    public PackageDestination(int id, int packageId, int destinationId) {
        this.id = id;
        this.packageId = packageId;
        this.destinationId = destinationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }


    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }
}




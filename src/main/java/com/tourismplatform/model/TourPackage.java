

package com.tourismplatform.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TourPackage {

    private int packageId;
    private String packageName;
    private String locationArea;
    private String description;
    private BigDecimal basePrice;
    private String imageUrl;
    private String status;
    private LocalDateTime createdAt;

    public TourPackage() {
    }

    public TourPackage(int packageId, String packageName, String locationArea, String description,
                       BigDecimal basePrice, String imageUrl, String status, LocalDateTime createdAt) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.locationArea = locationArea;
        this.description = description;
        this.basePrice = basePrice;
        this.imageUrl = imageUrl;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getPackageId() {
        return packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getLocationArea() {
        return locationArea;
    }

    public void setLocationArea(String locationArea) {
        this.locationArea = locationArea;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDestinationStatus() {
        return "";
    }

}

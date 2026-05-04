package com.tourismplatform.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Destination {

    private int destinationId;
    private String destinationName;
    private String description;
    private String imageUrl;
    private String googleMapUrl;
    private BigDecimal baseCost;
    private String status;
    private LocalDateTime createdAt;

    public Destination() {
    }

    public Destination(int destinationId, String destinationName, String description,
                       String imageUrl, String googleMapUrl, BigDecimal baseCost,
                       String status, LocalDateTime createdAt) {
        this.destinationId = destinationId;
        this.destinationName = destinationName;
        this.description = description;
        this.imageUrl = imageUrl;
        this.googleMapUrl = googleMapUrl;
        this.baseCost = baseCost;
        this.status = status;
        this.createdAt = createdAt;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getGoogleMapUrl() {
        return googleMapUrl;
    }

    public void setGoogleMapUrl(String googleMapUrl) {
        this.googleMapUrl = googleMapUrl;
    }

    public BigDecimal getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(BigDecimal baseCost) {
        this.baseCost = baseCost;
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
}

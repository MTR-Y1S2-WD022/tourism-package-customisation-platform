package com.tourismplatform.model;



import java.time.LocalDateTime;

    public class Review {

        private int reviewId;
        private int bookingId;
        private int userId;
        private int packageId;
        private int rating;
        private String comment;
        private String status;
        private LocalDateTime createdAt;

        public Review() {
        }

        public Review(int reviewId, int bookingId, int userId, int packageId, int rating, String comment, String status, LocalDateTime createdAt) {
            this.reviewId = reviewId;
            this.bookingId = bookingId;
            this.userId = userId;
            this.packageId = packageId;
            this.rating = rating;
            this.comment = comment;
            this.status = status;
            this.createdAt = createdAt;
        }

        public int getReviewId() {
            return reviewId;
        }

        public void setReviewId(int reviewId) {
            this.reviewId = reviewId;
        }

        public int getBookingId() {
            return bookingId;
        }

        public void setBookingId(int bookingId) {
            this.bookingId = bookingId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getPackageId() {
            return packageId;
        }

        public void setPackageId(int packageId) {
            this.packageId = packageId;
        }

        public int getRating() {
            return rating;
        }

        public void setRating(int rating) {
            this.rating = rating;
        }

        public String getComment() {
            return comment;
        }

        public void setComment(String comment) {
            this.comment = comment;
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


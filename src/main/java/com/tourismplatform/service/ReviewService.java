package com.tourismplatform.service;

import com.tourismplatform.dao.ReviewDAO;
import com.tourismplatform.model.Review;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewDAO reviewDAO;

    public ReviewService(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    public boolean addReview(Review review) {

        if (!isValidRating(review.getRating())) {
            return false;
        }

        if (reviewDAO.reviewExistsForBooking(review.getBookingId())) {
            return false;
        }

        review.setStatus("VISIBLE");

        return reviewDAO.addReview(review) > 0;
    }

    public List<Review> getAllVisibleReviews() {
        return reviewDAO.getAllVisibleReviews();
    }

    public List<Review> getAllReviewsForAdmin() {
        return reviewDAO.getAllReviewsForAdmin();
    }

    public List<Review> getReviewsByUserId(int userId) {
        return reviewDAO.getReviewsByUserId(userId);
    }

    public Review getReviewById(int reviewId) {
        return reviewDAO.getReviewById(reviewId);
    }

    public Review getReviewByBookingId(int bookingId) {
        return reviewDAO.getReviewByBookingId(bookingId);
    }

    public boolean updateReview(Review review) {

        if (!isValidRating(review.getRating())) {
            return false;
        }

        return reviewDAO.updateReview(review) > 0;
    }

    public boolean hideReview(int reviewId) {
        return reviewDAO.hideReview(reviewId) > 0;
    }

    public boolean showReview(int reviewId) {
        return reviewDAO.showReview(reviewId) > 0;
    }

    public boolean deleteReview(int reviewId) {
        return reviewDAO.deleteReview(reviewId) > 0;
    }

    public boolean reviewExistsForBooking(int bookingId) {
        return reviewDAO.reviewExistsForBooking(bookingId);
    }

    private boolean isValidRating(int rating) {
        return rating >= 1 && rating <= 5;
    }
}
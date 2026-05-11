package com.tourismplatform.controller;

import com.tourismplatform.model.Review;
import com.tourismplatform.service.BookingHistoryService;
import com.tourismplatform.service.ReviewService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReviewController {

    private final ReviewService reviewService;
    private final BookingHistoryService bookingHistoryService;

    public ReviewController(ReviewService reviewService, BookingHistoryService bookingHistoryService) {
        this.reviewService = reviewService;
        this.bookingHistoryService = bookingHistoryService;
    }

    @GetMapping("/reviews")
    public String viewVisibleReviews(Model model) {
        model.addAttribute("reviews", reviewService.getAllVisibleReviews());
        return "review/review-list";
    }

    @GetMapping("/reviews/new/{bookingId}")
    public String showReviewForm(@PathVariable int bookingId, Model model) {

        BookingHistoryService.BookingHistoryItem booking =
                bookingHistoryService.getBookingHistoryItemByBookingId(bookingId);

        if (!booking.isCanReview()) {
            model.addAttribute("errorMessage", "You can review only completed and paid tours that have not been reviewed yet.");
            model.addAttribute("reviews", reviewService.getAllVisibleReviews());
            return "review/review-list";
        }

        Review review = new Review();
        review.setBookingId(booking.getBookingId());
        review.setUserId(booking.getUserId());
        review.setPackageId(booking.getPackageId());

        model.addAttribute("review", review);
        model.addAttribute("booking", booking);

        return "review/review-form";
    }

    @PostMapping("/reviews/save")
    public String saveReview(@ModelAttribute Review review, Model model) {

        boolean saved = reviewService.addReview(review);

        if (!saved) {
            model.addAttribute("errorMessage", "Review could not be saved. Please check rating or duplicate review.");
            model.addAttribute("review", review);
            return "review/review-form";
        }

        return "redirect:/reviews";
    }

    @GetMapping("/reviews/edit/{reviewId}")
    public String showEditReviewForm(@PathVariable int reviewId, Model model) {

        Review review = reviewService.getReviewById(reviewId);

        model.addAttribute("review", review);

        return "review/review-form";
    }

    @PostMapping("/reviews/update")
    public String updateReview(@ModelAttribute Review review, Model model) {

        boolean updated = reviewService.updateReview(review);

        if (!updated) {
            model.addAttribute("errorMessage", "Review could not be updated. Rating must be between 1 and 5.");
            model.addAttribute("review", review);
            return "review/review-form";
        }

        return "redirect:/reviews";
    }

    @GetMapping("/admin/reviews")
    public String viewAdminReviews(Model model) {
        model.addAttribute("reviews", reviewService.getAllReviewsForAdmin());
        return "admin/review-list";
    }

    @GetMapping("/admin/reviews/hide/{reviewId}")
    public String hideReview(@PathVariable int reviewId) {
        reviewService.hideReview(reviewId);
        return "redirect:/admin/reviews";
    }

    @GetMapping("/admin/reviews/show/{reviewId}")
    public String showReview(@PathVariable int reviewId) {
        reviewService.showReview(reviewId);
        return "redirect:/admin/reviews";
    }

    @GetMapping("/admin/reviews/delete/{reviewId}")
    public String deleteReview(@PathVariable int reviewId) {
        reviewService.deleteReview(reviewId);
        return "redirect:/admin/reviews";
    }
}
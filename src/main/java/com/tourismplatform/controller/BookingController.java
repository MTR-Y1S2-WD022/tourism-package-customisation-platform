package com.tourismplatform.controller;

import com.tourismplatform.model.Booking;
import com.tourismplatform.service.BookingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/booking/customize/{packageId}")
    public String showCustomizeBookingPage(@PathVariable int packageId, Model model) {
        model.addAttribute("packageId", packageId);

        model.addAttribute("hotelTypes", List.of("BUDGET", "STANDARD", "LUXURY"));
        model.addAttribute("mealOptions", List.of("BREAKFAST_ONLY", "HALF_BOARD", "FULL_BOARD"));
        model.addAttribute("guideOptions", List.of("NO_GUIDE", "NORMAL_GUIDE", "PRO_GUIDE"));

        return "booking/customize-booking";
    }

    @PostMapping("/booking/summary")
    public String showBookingSummary(@RequestParam int userId,
                                     @RequestParam int packageId,
                                     @RequestParam(required = false) Integer couponId,
                                     @RequestParam LocalDate startDate,
                                     @RequestParam LocalDate endDate,
                                     @RequestParam String hotelType,
                                     @RequestParam String mealOption,
                                     @RequestParam String guideOption,
                                     @RequestParam BigDecimal packageBasePrice,
                                     @RequestParam(required = false) List<BigDecimal> selectedDestinationCosts,
                                     @RequestParam(required = false) List<Integer> selectedDestinationIds,
                                     @RequestParam(defaultValue = "0") BigDecimal discountAmount,
                                     @RequestParam(defaultValue = "1") int numberOfMembers,
                                     Model model) {

        if (!bookingService.isValidDateRange(startDate, endDate)) {
            model.addAttribute("errorMessage", "End date cannot be before start date.");
            return "booking/customize-booking";
        }

        BigDecimal subtotalAmount = bookingService.calculateSubtotal(
                packageBasePrice,
                selectedDestinationCosts,
                hotelType,
                mealOption,
                guideOption
        );

        // includes numberOfMembers in final amount calculation
        BigDecimal totalAmount = bookingService.calculateFinalAmountWithOOP(
                subtotalAmount,
                discountAmount,
                numberOfMembers
        );

        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setPackageId(packageId);
        booking.setCouponId(couponId);

        booking.setStartDate(startDate);
        booking.setEndDate(endDate);

        booking.setHotelType(hotelType);
        booking.setMealOption(mealOption);
        booking.setGuideOption(guideOption);

        booking.setSubtotalAmount(subtotalAmount);
        booking.setDiscountAmount(discountAmount);
        booking.setTotalAmount(totalAmount);

        booking.setNumberOfMembers(numberOfMembers);

        booking.setBookingStatus("PENDING");
        booking.setPaymentStatus("PENDING");

        model.addAttribute("booking", booking);
        model.addAttribute("selectedDestinationIds", selectedDestinationIds);

        return "booking/booking-summary";
    }

    @PostMapping("/booking/save")
    public String saveBooking(@ModelAttribute Booking booking,
                              @RequestParam(required = false) List<Integer> selectedDestinationIds) {

        booking.setBookingStatus("PENDING");
        booking.setPaymentStatus("PENDING");

        int bookingId = bookingService.saveBooking(booking);

        if (selectedDestinationIds != null) {
            for (Integer destinationId : selectedDestinationIds) {
                if (destinationId != null) {
                    bookingService.saveBookingDestination(bookingId, destinationId);
                }
            }
        }

        return "redirect:/booking/details/" + bookingId;
    }

    @GetMapping("/booking/details/{bookingId}")
    public String showBookingDetails(@PathVariable int bookingId, Model model) {
        Booking booking = bookingService.getBookingById(bookingId);
        List<Integer> destinationIds = bookingService.getDestinationIdsByBookingId(bookingId);

        model.addAttribute("booking", booking);
        model.addAttribute("destinationIds", destinationIds);

        return "booking/booking-details";
    }

    @GetMapping("/admin/bookings")
    public String showAdminBookingList(Model model) {
        List<Booking> bookings = bookingService.getAllBookings();

        model.addAttribute("bookings", bookings);

        return "admin/booking-list";
    }

    @GetMapping("/admin/bookings/cancel/{bookingId}")
    public String cancelBookingByAdmin(@PathVariable int bookingId) {
        bookingService.updateBookingStatus(bookingId, "CANCELLED");
        return "redirect:/admin/bookings";
    }
}

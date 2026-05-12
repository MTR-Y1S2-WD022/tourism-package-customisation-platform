package com.tourismplatform.controller;

import com.tourismplatform.service.BookingHistoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BookingHistoryController {

    private final BookingHistoryService bookingHistoryService;

    public BookingHistoryController(BookingHistoryService bookingHistoryService) {
        this.bookingHistoryService = bookingHistoryService;
    }

    @GetMapping("/history/bookings")
    public String viewBookingHistory(Model model) {

        int userId = 1;

        model.addAttribute("bookings", bookingHistoryService.getBookingHistoryByUserId(userId));

        return "history/booking-history";
    }

    @GetMapping("/history/bookings/{bookingId}")
    public String viewBookingHistoryDetails(@PathVariable int bookingId, Model model) {

        model.addAttribute("selectedBooking", bookingHistoryService.getBookingHistoryItemByBookingId(bookingId));

        int userId = 1;

        model.addAttribute("bookings", bookingHistoryService.getBookingHistoryByUserId(userId));

        return "history/booking-history";
    }
}

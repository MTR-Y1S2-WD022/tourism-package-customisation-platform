package com.tourismplatform.controller;

import com.tourismplatform.model.BookingCancellationRequest;
import com.tourismplatform.service.BookingCancellationRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cancellation")
public class BookingCancellationRequestController {

    private final BookingCancellationRequestService service;

    public BookingCancellationRequestController(BookingCancellationRequestService service) {
        this.service = service;
    }

    // Show cancel request form
    @GetMapping("/request/{bookingId}")
    public String showRequestForm(@PathVariable int bookingId, Model model) {
        BookingCancellationRequest request = new BookingCancellationRequest();
        request.setBookingId(bookingId);

        model.addAttribute("request", request);
        return "booking/cancel-request-form";
    }

    // Submit cancellation request
    @PostMapping("/request")
    public String submitRequest(@ModelAttribute BookingCancellationRequest request) {

        request.setUserId(1); // temporary user (later from session)

        service.createRequest(request);

        return "redirect:/booking/details/" + request.getBookingId();
    }

    // Admin view all requests
    @GetMapping("/admin/list")
    public String viewAllRequests(Model model) {

        model.addAttribute("requests", service.getAllRequests());
        return "admin/cancellation-list";
    }

    // Admin approve request
    @GetMapping("/admin/approve/{id}")
    public String approveRequest(@PathVariable int id) {

        service.processRequest(id, "APPROVED", "Approved by admin", 1);

        return "redirect:/cancellation/admin/list";
    }

    // Admin reject request
    @GetMapping("/admin/reject/{id}")
    public String rejectRequest(@PathVariable int id) {

        service.processRequest(id, "REJECTED", "Rejected by admin", 1);

        return "redirect:/cancellation/admin/list";
    }
}
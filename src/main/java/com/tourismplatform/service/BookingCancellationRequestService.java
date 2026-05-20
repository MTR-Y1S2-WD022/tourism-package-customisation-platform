package com.tourismplatform.service;

import com.tourismplatform.dao.BookingCancellationRequestDAO;
import com.tourismplatform.model.BookingCancellationRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingCancellationRequestService {

    private final BookingCancellationRequestDAO dao;

    public BookingCancellationRequestService(BookingCancellationRequestDAO dao) {
        this.dao = dao;
    }

    // Create cancellation request
    public boolean createRequest(BookingCancellationRequest request) {

        // Business rule: always set default status
        request.setRequestStatus("PENDING");
        request.setRefundType("NONE");
        request.setRefundAmount(0.0);

        return dao.createRequest(request) > 0;
    }

    // Get all requests (admin view)
    public List<BookingCancellationRequest> getAllRequests() {
        return dao.getAllRequests();
    }

    // Approve / Reject request
    public boolean processRequest(int requestId, String status, String response, Integer adminId) {
        return dao.updateStatus(requestId, status, response, adminId) > 0;
    }
}
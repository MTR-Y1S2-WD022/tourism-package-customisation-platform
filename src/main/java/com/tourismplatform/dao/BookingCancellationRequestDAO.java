package com.tourismplatform.dao;

import com.tourismplatform.model.BookingCancellationRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingCancellationRequestDAO {

    private final JdbcTemplate jdbcTemplate;

    public BookingCancellationRequestDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // CREATE cancellation request
    public int createRequest(BookingCancellationRequest request) {
        String sql = "INSERT INTO booking_cancellation_requests " +
                "(booking_id, user_id, reason, request_status, refund_type, refund_amount) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        return jdbcTemplate.update(sql,
                request.getBookingId(),
                request.getUserId(),
                request.getReason(),
                request.getRequestStatus(),
                request.getRefundType(),
                request.getRefundAmount()
        );
    }

    // GET all requests (for admin)
    public List<BookingCancellationRequest> getAllRequests() {
        String sql = "SELECT * FROM booking_cancellation_requests";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            BookingCancellationRequest r = new BookingCancellationRequest();

            r.setRequestId(rs.getInt("request_id"));
            r.setBookingId(rs.getInt("booking_id"));
            r.setUserId(rs.getInt("user_id"));
            r.setReason(rs.getString("reason"));
            r.setRequestStatus(rs.getString("request_status"));
            r.setAdminId(rs.getObject("admin_id") != null ? rs.getInt("admin_id") : null);
            r.setAdminResponse(rs.getString("admin_response"));
            r.setRefundType(rs.getString("refund_type"));
            r.setRefundAmount(rs.getDouble("refund_amount"));
            r.setRequestedAt(rs.getTimestamp("requested_at").toLocalDateTime());
            if (rs.getTimestamp("processed_at") != null) {
                r.setProcessedAt(rs.getTimestamp("processed_at").toLocalDateTime());
            }

            return r;
        });
    }

    // UPDATE request status (approve/reject)
    public int updateStatus(int requestId, String status, String response, Integer adminId) {
        String sql = "UPDATE booking_cancellation_requests " +
                "SET request_status = ?, admin_response = ?, admin_id = ?, processed_at = NOW() " +
                "WHERE request_id = ?";

        return jdbcTemplate.update(sql, status, response, adminId, requestId);
    }
}
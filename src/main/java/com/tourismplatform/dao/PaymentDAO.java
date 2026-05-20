package com.tourismplatform.dao;

import com.tourismplatform.model.Payment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class PaymentDAO {

    private final JdbcTemplate jdbcTemplate;

    public PaymentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Payment> paymentRowMapper = new RowMapper<Payment>() {
        @Override
        public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Payment payment = new Payment();

            payment.setPaymentId(rs.getInt("payment_id"));
            payment.setBookingId(rs.getInt("booking_id"));
            payment.setUserId(rs.getInt("user_id"));
            payment.setPaymentMethod(rs.getString("payment_method"));
            payment.setAmount(rs.getBigDecimal("amount"));
            payment.setPaymentStatus(rs.getString("payment_status"));

            if (rs.getTimestamp("payment_date") != null) {
                payment.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
            }

            payment.setRefundAmount(rs.getBigDecimal("refund_amount"));
            payment.setRefundStatus(rs.getString("refund_status"));

            return payment;
        }
    };

    public int savePayment(Payment payment) {
        String sql = """
                INSERT INTO payments
                (booking_id, user_id, payment_method, amount, payment_status, refund_amount, refund_status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                payment.getBookingId(),
                payment.getUserId(),
                payment.getPaymentMethod(),
                payment.getAmount(),
                payment.getPaymentStatus(),
                payment.getRefundAmount(),
                payment.getRefundStatus()
        );
    }

    public Payment findPaymentById(int paymentId) {
        String sql = """
                SELECT payment_id, booking_id, user_id, payment_method, amount,
                       payment_status, payment_date, refund_amount, refund_status
                FROM payments
                WHERE payment_id = ?
                """;

        return jdbcTemplate.queryForObject(sql, paymentRowMapper, paymentId);
    }

    public Payment findPaymentByBookingId(int bookingId) {
        String sql = """
                SELECT payment_id, booking_id, user_id, payment_method, amount,
                       payment_status, payment_date, refund_amount, refund_status
                FROM payments
                WHERE booking_id = ?
                """;

        return jdbcTemplate.queryForObject(sql, paymentRowMapper, bookingId);
    }

    public List<Payment> findPaymentsByUserId(int userId) {
        String sql = """
                SELECT payment_id, booking_id, user_id, payment_method, amount,
                       payment_status, payment_date, refund_amount, refund_status
                FROM payments
                WHERE user_id = ?
                ORDER BY payment_date DESC
                """;

        return jdbcTemplate.query(sql, paymentRowMapper, userId);
    }

    public List<Payment> findAllPayments() {
        String sql = """
                SELECT payment_id, booking_id, user_id, payment_method, amount,
                       payment_status, payment_date, refund_amount, refund_status
                FROM payments
                ORDER BY payment_date DESC
                """;

        return jdbcTemplate.query(sql, paymentRowMapper);
    }

    public int updatePaymentStatus(int paymentId, String paymentStatus) {
        String sql = """
                UPDATE payments
                SET payment_status = ?
                WHERE payment_id = ?
                """;

        return jdbcTemplate.update(sql, paymentStatus, paymentId);
    }

    public int updateRefundDetails(int paymentId, String paymentStatus,
                                   String refundStatus, BigDecimal refundAmount) {
        String sql = """
                UPDATE payments
                SET payment_status = ?,
                    refund_status = ?,
                    refund_amount = ?
                WHERE payment_id = ?
                """;

        return jdbcTemplate.update(sql, paymentStatus, refundStatus, refundAmount, paymentId);
    }

    public int updateBookingPaymentStatus(int bookingId, String paymentStatus) {
        String sql = """
                UPDATE bookings
                SET payment_status = ?
                WHERE booking_id = ?
                """;

        return jdbcTemplate.update(sql, paymentStatus, bookingId);
    }

    public int deletePayment(int paymentId) {
        String sql = """
                DELETE FROM payments
                WHERE payment_id = ?
                """;

        return jdbcTemplate.update(sql, paymentId);
    }
}
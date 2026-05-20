package com.tourismplatform.service;

import com.tourismplatform.dao.PaymentDAO;
import com.tourismplatform.model.Payment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentDAO paymentDAO;

    public PaymentService(PaymentDAO paymentDAO) {
        this.paymentDAO = paymentDAO;
    }

    public int createPayment(Payment payment) {
        String paymentMethod = payment.getPaymentMethod();

        if ("CARD".equalsIgnoreCase(paymentMethod)) {
            payment.setPaymentStatus("PAID");
        } else if ("BANK_TRANSFER".equalsIgnoreCase(paymentMethod)) {
            payment.setPaymentStatus("PENDING");
        } else {
            throw new IllegalArgumentException("Invalid payment method");
        }

        if (payment.getRefundAmount() == null) {
            payment.setRefundAmount(BigDecimal.ZERO);
        }

        if (payment.getRefundStatus() == null || payment.getRefundStatus().isBlank()) {
            payment.setRefundStatus("NONE");
        }

        int result = paymentDAO.savePayment(payment);

        syncBookingPaymentStatus(payment.getBookingId(), payment.getPaymentStatus());

        return result;
    }

    public Payment getPaymentById(int paymentId) {
        return paymentDAO.findPaymentById(paymentId);
    }

    public Payment getPaymentByBookingId(int bookingId) {
        return paymentDAO.findPaymentByBookingId(bookingId);
    }

    public List<Payment> getPaymentsByUserId(int userId) {
        return paymentDAO.findPaymentsByUserId(userId);
    }

    public List<Payment> getAllPayments() {
        return paymentDAO.findAllPayments();
    }

    public int updatePaymentStatus(int paymentId, String paymentStatus) {
        if (!isValidAdminPaymentStatus(paymentStatus)) {
            throw new IllegalArgumentException("Invalid payment status");
        }

        Payment payment = paymentDAO.findPaymentById(paymentId);

        int result = paymentDAO.updatePaymentStatus(paymentId, paymentStatus);

        syncBookingPaymentStatus(payment.getBookingId(), paymentStatus);

        return result;
    }

    public int cancelPayment(int paymentId) {
        Payment payment = paymentDAO.findPaymentById(paymentId);

        int result = paymentDAO.updatePaymentStatus(paymentId, "CANCELLED");

        syncBookingPaymentStatus(payment.getBookingId(), "CANCELLED");

        return result;
    }

    public int processFullRefund(int bookingId) {
        Payment payment = paymentDAO.findPaymentByBookingId(bookingId);

        if (!"PAID".equalsIgnoreCase(payment.getPaymentStatus())) {
            throw new IllegalArgumentException("Only PAID payments can be refunded");
        }

        BigDecimal refundAmount = payment.getAmount();

        int result = paymentDAO.updateRefundDetails(
                payment.getPaymentId(),
                "REFUNDED",
                "FULL_REFUNDED",
                refundAmount
        );

        syncBookingPaymentStatus(bookingId, "REFUNDED");

        return result;
    }

    public int processCustomRefund(int bookingId, BigDecimal refundAmount) {
        Payment payment = paymentDAO.findPaymentByBookingId(bookingId);

        if (!"PAID".equalsIgnoreCase(payment.getPaymentStatus())) {
            throw new IllegalArgumentException("Only PAID payments can be refunded");
        }

        validateRefundAmount(payment.getAmount(), refundAmount);

        int result = paymentDAO.updateRefundDetails(
                payment.getPaymentId(),
                "PARTIALLY_REFUNDED",
                "PARTIALLY_REFUNDED",
                refundAmount
        );

        syncBookingPaymentStatus(bookingId, "PARTIALLY_REFUNDED");

        return result;
    }

    public void validateRefundAmount(BigDecimal paidAmount, BigDecimal refundAmount) {
        if (refundAmount == null) {
            throw new IllegalArgumentException("Refund amount is required");
        }

        if (refundAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Refund amount cannot be negative");
        }

        if (refundAmount.compareTo(paidAmount) > 0) {
            throw new IllegalArgumentException("Refund amount cannot be greater than paid amount");
        }
    }

    public void syncBookingPaymentStatus(int bookingId, String paymentStatus) {
        paymentDAO.updateBookingPaymentStatus(bookingId, paymentStatus);
    }

    public int deletePayment(int paymentId) {
        return paymentDAO.deletePayment(paymentId);
    }

    private boolean isValidAdminPaymentStatus(String paymentStatus) {
        return "PAID".equalsIgnoreCase(paymentStatus)
                || "FAILED".equalsIgnoreCase(paymentStatus)
                || "CANCELLED".equalsIgnoreCase(paymentStatus)
                || "REFUNDED".equalsIgnoreCase(paymentStatus)
                || "PARTIALLY_REFUNDED".equalsIgnoreCase(paymentStatus);
    }
}
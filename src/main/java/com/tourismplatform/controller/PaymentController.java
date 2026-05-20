package com.tourismplatform.controller;

import com.tourismplatform.model.Payment;
import com.tourismplatform.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/payments/new/{bookingId}")
    public String showPaymentForm(@PathVariable int bookingId, Model model) {

        Payment payment = new Payment();
        payment.setBookingId(bookingId);

        /*
         * Temporary values for beginner testing.
         * Later, userId and amount should come from the logged-in user and booking summary.
         */
        payment.setUserId(1);
        payment.setAmount(new BigDecimal("50000.00"));

        model.addAttribute("payment", payment);

        return "payment/payment-form";
    }

    @PostMapping("/payments/pay")
    public String createPayment(@ModelAttribute Payment payment) {

        paymentService.createPayment(payment);

        return "redirect:/payments/user/" + payment.getUserId();
    }

    @GetMapping("/payments/details/{paymentId}")
    public String viewPaymentDetails(@PathVariable int paymentId, Model model) {

        Payment payment = paymentService.getPaymentById(paymentId);

        model.addAttribute("payment", payment);

        return "payment/payment-details";
    }

    @GetMapping("/payments/user/{userId}")
    public String viewUserPayments(@PathVariable int userId, Model model) {

        model.addAttribute("payments", paymentService.getPaymentsByUserId(userId));
        model.addAttribute("userId", userId);

        return "payment/payment-list";
    }

    @GetMapping("/admin/payments")
    public String viewAllPaymentsForAdmin(Model model) {

        model.addAttribute("payments", paymentService.getAllPayments());

        return "admin/payment-list";
    }

    @PostMapping("/admin/payments/status/{paymentId}")
    public String updatePaymentStatus(@PathVariable int paymentId,
                                      @RequestParam String paymentStatus) {

        paymentService.updatePaymentStatus(paymentId, paymentStatus);

        return "redirect:/admin/payments";
    }

    @PostMapping("/admin/payments/cancel/{paymentId}")
    public String cancelPayment(@PathVariable int paymentId) {

        paymentService.cancelPayment(paymentId);

        return "redirect:/admin/payments";
    }

    @PostMapping("/admin/payments/delete/{paymentId}")
    public String deletePayment(@PathVariable int paymentId) {

        paymentService.deletePayment(paymentId);

        return "redirect:/admin/payments";
    }

    @PostMapping("/admin/payments/refund/full/{bookingId}")
    public String processFullRefund(@PathVariable int bookingId) {

        paymentService.processFullRefund(bookingId);

        return "redirect:/admin/payments";
    }

    @PostMapping("/admin/payments/refund/custom/{bookingId}")
    public String processCustomRefund(@PathVariable int bookingId,
                                      @RequestParam BigDecimal refundAmount) {

        paymentService.processCustomRefund(bookingId, refundAmount);

        return "redirect:/admin/payments";
    }
}
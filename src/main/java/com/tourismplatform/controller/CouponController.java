package com.tourismplatform.controller;

import com.tourismplatform.model.PercentageCoupon;
import com.tourismplatform.model.Coupon;

import com.tourismplatform.service.CouponService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

import java.util.List;


@Controller
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/admin/coupons")
    public String redirectToCouponDashboard() {

        return "redirect:/admin/coupons/dashboard";
    }

    @GetMapping("/admin/coupons/new")
    public String showAddCouponForm(Model model) {
        model.addAttribute("coupon", new PercentageCoupon());
        return "coupon/coupon-form";
    }

    @PostMapping("/admin/coupons/save")
    public String saveCoupon(@ModelAttribute PercentageCoupon coupon) {
        couponService.addCoupon(coupon);
        return "redirect:/admin/coupons";
    }

    @GetMapping("/admin/coupons/edit/{couponId}")
    public String showEditCouponForm(@PathVariable int couponId, Model model) {
        Coupon coupon = couponService.getCouponById(couponId);
        model.addAttribute("coupon", coupon);
        return "coupon/coupon-form";
    }

    @PostMapping("/admin/coupons/update")
    public String updateCoupon(@ModelAttribute Coupon coupon) {
        couponService.updateCoupon(coupon);
        return "redirect:/admin/coupons";
    }

    @GetMapping("/admin/coupons/cancel/{couponId}")
    public String cancelCoupon(@PathVariable int couponId) {
        couponService.cancelCoupon(couponId);
        return "redirect:/admin/coupons";
    }

    @PostMapping("/admin/coupons/extend/{couponId}")
    public String extendCouponExpiryDate(@PathVariable int couponId,
                                         @RequestParam LocalDate newExpiryDate) {
        couponService.extendExpiryDate(couponId, newExpiryDate);
        return "redirect:/admin/coupons";
    }

    @GetMapping("/coupons/validate")
    @ResponseBody
    public String validateCoupon(@RequestParam String couponCode,
                                 @RequestParam double subtotalAmount) {

        Coupon coupon = couponService.findCouponByCode(couponCode);

        if (!couponService.isCouponValid(coupon)) {
            return "INVALID";
        }

        double discountAmount = couponService.calculateDiscountAmount(coupon, subtotalAmount);
        double finalTotal = couponService.calculateFinalTotal(subtotalAmount, discountAmount);


        return "VALID | Discount: " + discountAmount + " | Final Total: " + finalTotal;
    }
    @GetMapping("/admin/coupons/dashboard")
    public String couponDashboard(Model model) {
        List<Coupon> coupons = couponService.getAllCoupons();

        long totalCoupons = coupons.size();
        long activeCoupons = coupons.stream()
                .filter(coupon -> "ACTIVE".equalsIgnoreCase(coupon.getStatus()))
                .count();
        long expiredCoupons = coupons.stream()
                .filter(coupon -> "EXPIRED".equalsIgnoreCase(coupon.getStatus()))
                .count();
        long cancelledCoupons = coupons.stream()
                .filter(coupon -> "CANCELLED".equalsIgnoreCase(coupon.getStatus()))
                .count();

        model.addAttribute("totalCoupons", totalCoupons);
        model.addAttribute("activeCoupons", activeCoupons);
        model.addAttribute("expiredCoupons", expiredCoupons);
        model.addAttribute("cancelledCoupons", cancelledCoupons);

        return "coupon/coupon-dashboard";

    }
    @GetMapping("/admin/coupons/list")
    public String viewCouponsList(Model model) {
        model.addAttribute("coupons", couponService.getAllCoupons());
        return "coupon/coupon-list";
    }
}

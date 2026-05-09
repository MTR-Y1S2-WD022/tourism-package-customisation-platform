package com.tourismplatform.controller;

import com.tourismplatform.model.Coupon;
import com.tourismplatform.service.CouponService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Controller
public class CouponController {

    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @GetMapping("/admin/coupons")
    public String viewCoupons(Model model) {
        model.addAttribute("coupons", couponService.getAllCoupons());
        return "coupon/coupon-list";
    }

    @GetMapping("/admin/coupons/new")
    public String showAddCouponForm(Model model) {
        model.addAttribute("coupon", new Coupon());
        return "coupon/coupon-form";
    }

    @PostMapping("/admin/coupons/save")
    public String saveCoupon(@ModelAttribute Coupon coupon) {
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
                                 @RequestParam BigDecimal subtotalAmount) {

        Coupon coupon = couponService.findCouponByCode(couponCode);

        if (!couponService.isCouponValid(coupon)) {
            return "INVALID";
        }

        BigDecimal discountAmount = couponService.calculateDiscountAmount(coupon, subtotalAmount);
        BigDecimal finalTotal = couponService.calculateFinalTotal(subtotalAmount, discountAmount);

        return "VALID | Discount: " + discountAmount + " | Final Total: " + finalTotal;
    }
}

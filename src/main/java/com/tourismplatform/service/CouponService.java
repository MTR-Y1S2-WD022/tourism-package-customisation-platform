package com.tourismplatform.service;

import com.tourismplatform.dao.CouponDAO;
import com.tourismplatform.model.Coupon;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CouponService {

    private final CouponDAO couponDAO;

    public CouponService(CouponDAO couponDAO) {
        this.couponDAO = couponDAO;
    }

    public void addCoupon(Coupon coupon) {
        if (coupon.getStatus() == null || coupon.getStatus().isBlank()) {
            coupon.setStatus("ACTIVE");
        }

        couponDAO.saveCoupon(coupon);
    }

    public List<Coupon> getAllCoupons() {
        return couponDAO.getAllCoupons();
    }

    public Coupon getCouponById(int couponId) {
        return couponDAO.getCouponById(couponId);
    }

    public void updateCoupon(Coupon coupon) {
        couponDAO.updateCoupon(coupon);
    }

    public void extendExpiryDate(int couponId, LocalDate newExpiryDate) {
        couponDAO.extendExpiryDate(couponId, newExpiryDate);
    }

    public void cancelCoupon(int couponId) {
        couponDAO.cancelCoupon(couponId);
    }

    public Coupon findCouponByCode(String couponCode) {
        try {
            return couponDAO.getCouponByCode(couponCode);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public boolean isCouponValid(Coupon coupon) {
        if (coupon == null) {
            return false;
        }

        if (coupon.getCouponCode() == null || coupon.getCouponCode().isBlank()) {
            return false;
        }

        if (!"ACTIVE".equalsIgnoreCase(coupon.getStatus())) {
            return false;
        }

        LocalDate today = LocalDate.now();

        if (coupon.getIssueDate() != null && today.isBefore(coupon.getIssueDate())) {
            return false;
        }

        if (coupon.getExpiryDate() != null && today.isAfter(coupon.getExpiryDate())) {
            return false;
        }

        if (coupon.getDiscountValue() <= 0){
            return false;
        }
        return true;
    }

    public double calculateDiscountAmount(Coupon coupon, double subtotalAmount) {

        if (!isCouponValid(coupon) || subtotalAmount <= 0) {
            return 0;
        }

        double discountAmount = coupon.calculateDiscount(subtotalAmount);

        if (discountAmount > subtotalAmount) {
            return subtotalAmount;
        }

        return discountAmount;
    }

    public double calculateFinalTotal(double subtotalAmount, double discountAmount) {

        double finalTotal = subtotalAmount - discountAmount;

        if (finalTotal < 0) {
            return 0;
        }

        return finalTotal;

    }
}

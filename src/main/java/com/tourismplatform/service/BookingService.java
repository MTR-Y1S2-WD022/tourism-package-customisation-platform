package com.tourismplatform.service;

import com.tourismplatform.dao.BookingDAO;
import com.tourismplatform.model.BookingEntity;
import com.tourismplatform.model.Booking;
import com.tourismplatform.model.NormalBooking;
import com.tourismplatform.model.CouponBooking;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    private final BookingDAO bookingDAO;

    public BookingService(BookingDAO bookingDAO) {
        this.bookingDAO = bookingDAO;
    }

    // =========================
    // COST CALCULATIONS
    // =========================

    public BigDecimal getHotelCost(String hotelType) {
        return switch (hotelType) {
            case "BUDGET" -> BigDecimal.valueOf(5000);
            case "STANDARD" -> BigDecimal.valueOf(10000);
            case "LUXURY" -> BigDecimal.valueOf(20000);
            default -> BigDecimal.ZERO;
        };
    }

    public BigDecimal getMealCost(String mealOption) {
        return switch (mealOption) {
            case "BREAKFAST_ONLY" -> BigDecimal.valueOf(1500);
            case "HALF_BOARD" -> BigDecimal.valueOf(3000);
            case "FULL_BOARD" -> BigDecimal.valueOf(5000);
            default -> BigDecimal.ZERO;
        };
    }

    public BigDecimal getGuideCost(String guideOption) {
        return switch (guideOption) {
            case "NO_GUIDE" -> BigDecimal.ZERO;
            case "NORMAL_GUIDE" -> BigDecimal.valueOf(5000);
            case "PRO_GUIDE" -> BigDecimal.valueOf(10000);
            default -> BigDecimal.ZERO;
        };
    }

    // =========================
    // SUBTOTAL CALCULATION
    // =========================

    public BigDecimal calculateSubtotal(BigDecimal packageBasePrice,
                                        List<BigDecimal> selectedDestinationCosts,
                                        String hotelType,
                                        String mealOption,
                                        String guideOption) {

        BigDecimal destinationTotal = BigDecimal.ZERO;

        if (selectedDestinationCosts != null) {
            for (BigDecimal cost : selectedDestinationCosts) {
                if (cost != null) {
                    destinationTotal = destinationTotal.add(cost);
                }
            }
        }

        return packageBasePrice
                .add(destinationTotal)
                .add(getHotelCost(hotelType))
                .add(getMealCost(mealOption))
                .add(getGuideCost(guideOption));
    }

    // =========================
    // DISCOUNT CALCULATION
    // =========================

    public BigDecimal calculateDiscount(BigDecimal subtotalAmount,
                                        String discountType,
                                        BigDecimal discountValue) {

        if (discountType == null || discountValue == null) {
            return BigDecimal.ZERO;
        }

        if ("PERCENTAGE".equals(discountType)) {
            return subtotalAmount.multiply(discountValue)
                    .divide(BigDecimal.valueOf(100));
        }

        if ("FIXED".equals(discountType)) {
            if (discountValue.compareTo(subtotalAmount) > 0) {
                return subtotalAmount;
            }
            return discountValue;
        }

        return BigDecimal.ZERO;
    }

    // =========================
    // FINAL TOTAL (OLD LOGIC)
    // =========================

    public BigDecimal calculateFinalTotal(BigDecimal subtotalAmount, BigDecimal discountAmount) {
        BigDecimal finalTotal = subtotalAmount.subtract(discountAmount);

        if (finalTotal.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO;
        }

        return finalTotal;
    }

    // =========================
    // OOP POLYMORPHISM METHOD (NEW)
    // =========================

    public BigDecimal calculateFinalAmountWithOOP(BigDecimal subtotal, BigDecimal discount) {

        Booking booking;

        if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
            booking = new CouponBooking();
        } else {
            booking = new NormalBooking();
        }

        booking.setSubtotalAmount(subtotal.doubleValue());
        booking.setDiscountAmount(discount.doubleValue());

        double result = booking.calculateTotal();

        return BigDecimal.valueOf(result);
    }

    // =========================
    // VALIDATION
    // =========================

    public boolean isValidDateRange(LocalDate startDate, LocalDate endDate) {
        return startDate != null
                && endDate != null
                && !endDate.isBefore(startDate);
    }

    // =========================
    // DATABASE OPERATIONS
    // =========================

    public int saveBooking(BookingEntity booking) {
        return bookingDAO.saveBooking(booking);
    }

    public void saveBookingDestination(int bookingId, int destinationId) {
        bookingDAO.saveBookingDestination(bookingId, destinationId);
    }

    public List<BookingEntity> getAllBookings() {
        return bookingDAO.findAllBookings();
    }

    public BookingEntity getBookingById(int bookingId) {
        return bookingDAO.findBookingById(bookingId);
    }

    public List<Integer> getDestinationIdsByBookingId(int bookingId) {
        return bookingDAO.findDestinationIdsByBookingId(bookingId);
    }

    public void updateBookingStatus(int bookingId, String bookingStatus) {
        bookingDAO.updateBookingStatus(bookingId, bookingStatus);
    }

    public void cancelBooking(int bookingId) {
        bookingDAO.cancelBooking(bookingId);
    }

    // =========================
    // GOOGLE MAPS
    // =========================

    public String generateGoogleMapsUrl(List<String> destinationNames) {
        String baseUrl = "https://www.google.com/maps/search/?api=1&query=";

        if (destinationNames == null || destinationNames.isEmpty()) {
            return baseUrl;
        }

        String query = String.join(" ", destinationNames);
        String formattedQuery = query.replace(" ", "+");

        return baseUrl + formattedQuery;
    }
}
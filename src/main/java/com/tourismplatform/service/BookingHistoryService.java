package com.tourismplatform.service;

import com.tourismplatform.util.DateStatusUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class BookingHistoryService {

    private final JdbcTemplate jdbcTemplate;

    public BookingHistoryService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<BookingHistoryItem> getBookingHistoryByUserId(int userId) {
        String sql = """
                SELECT
                    b.booking_id,
                    b.user_id,
                    b.package_id,
                    tp.package_name,
                    b.start_date,
                    b.end_date,
                    b.booking_status,
                    COALESCE(p.payment_status, b.payment_status, 'PENDING') AS payment_status,
                    CASE
                        WHEN r.review_id IS NULL THEN 0
                        ELSE 1
                    END AS review_exists
                FROM bookings b
                JOIN tour_packages tp ON b.package_id = tp.package_id
                LEFT JOIN payments p ON b.booking_id = p.booking_id
                LEFT JOIN reviews r ON b.booking_id = r.booking_id
                WHERE b.user_id = ?
                ORDER BY b.booking_id DESC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            BookingHistoryItem item = new BookingHistoryItem();

            item.setBookingId(rs.getInt("booking_id"));
            item.setUserId(rs.getInt("user_id"));
            item.setPackageId(rs.getInt("package_id"));
            item.setPackageName(rs.getString("package_name"));

            if (rs.getDate("start_date") != null) {
                item.setStartDate(rs.getDate("start_date").toLocalDate());
            }

            if (rs.getDate("end_date") != null) {
                item.setEndDate(rs.getDate("end_date").toLocalDate());
            }

            item.setBookingStatus(rs.getString("booking_status"));
            item.setPaymentStatus(rs.getString("payment_status"));
            item.setReviewExists(rs.getBoolean("review_exists"));

            String tourStatus = DateStatusUtil.calculateTourStatus(
                    item.getBookingStatus(),
                    item.getStartDate(),
                    item.getEndDate()
            );

            item.setTourStatus(tourStatus);

            boolean canReview = "COMPLETED".equalsIgnoreCase(tourStatus)
                    && "PAID".equalsIgnoreCase(item.getPaymentStatus())
                    && !item.isReviewExists();

            item.setCanReview(canReview);

            return item;
        }, userId);
    }

    public BookingHistoryItem getBookingHistoryItemByBookingId(int bookingId) {
        String sql = """
                SELECT
                    b.booking_id,
                    b.user_id,
                    b.package_id,
                    tp.package_name,
                    b.start_date,
                    b.end_date,
                    b.booking_status,
                    COALESCE(p.payment_status, b.payment_status, 'PENDING') AS payment_status,
                    CASE
                        WHEN r.review_id IS NULL THEN 0
                        ELSE 1
                    END AS review_exists
                FROM bookings b
                JOIN tour_packages tp ON b.package_id = tp.package_id
                LEFT JOIN payments p ON b.booking_id = p.booking_id
                LEFT JOIN reviews r ON b.booking_id = r.booking_id
                WHERE b.booking_id = ?
                """;

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            BookingHistoryItem item = new BookingHistoryItem();

            item.setBookingId(rs.getInt("booking_id"));
            item.setUserId(rs.getInt("user_id"));
            item.setPackageId(rs.getInt("package_id"));
            item.setPackageName(rs.getString("package_name"));

            if (rs.getDate("start_date") != null) {
                item.setStartDate(rs.getDate("start_date").toLocalDate());
            }

            if (rs.getDate("end_date") != null) {
                item.setEndDate(rs.getDate("end_date").toLocalDate());
            }

            item.setBookingStatus(rs.getString("booking_status"));
            item.setPaymentStatus(rs.getString("payment_status"));
            item.setReviewExists(rs.getBoolean("review_exists"));

            String tourStatus = DateStatusUtil.calculateTourStatus(
                    item.getBookingStatus(),
                    item.getStartDate(),
                    item.getEndDate()
            );

            item.setTourStatus(tourStatus);

            boolean canReview = "COMPLETED".equalsIgnoreCase(tourStatus)
                    && "PAID".equalsIgnoreCase(item.getPaymentStatus())
                    && !item.isReviewExists();

            item.setCanReview(canReview);

            return item;
        }, bookingId);
    }

    public static class BookingHistoryItem {

        private int bookingId;
        private int userId;
        private int packageId;
        private String packageName;
        private LocalDate startDate;
        private LocalDate endDate;
        private String bookingStatus;
        private String paymentStatus;
        private String tourStatus;
        private boolean reviewExists;
        private boolean canReview;

        public int getBookingId() {
            return bookingId;
        }

        public void setBookingId(int bookingId) {
            this.bookingId = bookingId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getPackageId() {
            return packageId;
        }

        public void setPackageId(int packageId) {
            this.packageId = packageId;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public LocalDate getStartDate() {
            return startDate;
        }

        public void setStartDate(LocalDate startDate) {
            this.startDate = startDate;
        }

        public LocalDate getEndDate() {
            return endDate;
        }

        public void setEndDate(LocalDate endDate) {
            this.endDate = endDate;
        }

        public String getBookingStatus() {
            return bookingStatus;
        }

        public void setBookingStatus(String bookingStatus) {
            this.bookingStatus = bookingStatus;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getTourStatus() {
            return tourStatus;
        }

        public void setTourStatus(String tourStatus) {
            this.tourStatus = tourStatus;
        }

        public boolean isReviewExists() {
            return reviewExists;
        }

        public void setReviewExists(boolean reviewExists) {
            this.reviewExists = reviewExists;
        }

        public boolean isCanReview() {
            return canReview;
        }

        public void setCanReview(boolean canReview) {
            this.canReview = canReview;
        }
    }
}

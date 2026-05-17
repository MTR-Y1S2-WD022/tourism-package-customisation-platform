package com.tourismplatform.dao;

import com.tourismplatform.model.BookingEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;

@Repository
public class BookingDAO {

    private final JdbcTemplate jdbcTemplate;

    public BookingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<BookingEntity> bookingRowMapper = (rs, rowNum) -> {
        BookingEntity booking = new BookingEntity();

        booking.setBookingId(rs.getInt("booking_id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setPackageId(rs.getInt("package_id"));

        int couponId = rs.getInt("coupon_id");
        if (rs.wasNull()) {
            booking.setCouponId(null);
        } else {
            booking.setCouponId(couponId);
        }

        booking.setStartDate(rs.getDate("start_date").toLocalDate());
        booking.setEndDate(rs.getDate("end_date").toLocalDate());
        booking.setHotelType(rs.getString("hotel_type"));
        booking.setMealOption(rs.getString("meal_option"));
        booking.setGuideOption(rs.getString("guide_option"));
        booking.setSubtotalAmount(rs.getBigDecimal("subtotal_amount"));
        booking.setDiscountAmount(rs.getBigDecimal("discount_amount"));
        booking.setTotalAmount(rs.getBigDecimal("total_amount"));
        booking.setBookingStatus(rs.getString("booking_status"));
        booking.setPaymentStatus(rs.getString("payment_status"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            booking.setCreatedAt(createdAt.toLocalDateTime());
        }

        return booking;
    };

    public int saveBooking(BookingEntity booking) {
        String sql = """
                INSERT INTO bookings
                (user_id, package_id, coupon_id, start_date, end_date,
                 hotel_type, meal_option, guide_option,
                 subtotal_amount, discount_amount, total_amount,
                 booking_status, payment_status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getPackageId());

            if (booking.getCouponId() == null) {
                ps.setNull(3, Types.INTEGER);
            } else {
                ps.setInt(3, booking.getCouponId());
            }

            ps.setDate(4, java.sql.Date.valueOf(booking.getStartDate()));
            ps.setDate(5, java.sql.Date.valueOf(booking.getEndDate()));
            ps.setString(6, booking.getHotelType());
            ps.setString(7, booking.getMealOption());
            ps.setString(8, booking.getGuideOption());
            ps.setBigDecimal(9, booking.getSubtotalAmount());
            ps.setBigDecimal(10, booking.getDiscountAmount());
            ps.setBigDecimal(11, booking.getTotalAmount());
            ps.setString(12, booking.getBookingStatus());
            ps.setString(13, booking.getPaymentStatus());

            return ps;
        }, keyHolder);

        Number generatedId = keyHolder.getKey();

        if (generatedId == null) {
            return 0;
        }

        return generatedId.intValue();
    }

    public void saveBookingDestination(int bookingId, int destinationId) {
        String sql = """
                INSERT INTO booking_destinations
                (booking_id, destination_id)
                VALUES (?, ?)
                """;

        jdbcTemplate.update(sql, bookingId, destinationId);
    }

    public List<BookingEntity> findAllBookings() {
        String sql = "SELECT * FROM bookings ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, bookingRowMapper);
    }

    public BookingEntity findBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        return jdbcTemplate.queryForObject(sql, bookingRowMapper, bookingId);
    }

    public List<Integer> findDestinationIdsByBookingId(int bookingId) {
        String sql = "SELECT destination_id FROM booking_destinations WHERE booking_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, bookingId);
    }

    public void updateBookingStatus(int bookingId, String bookingStatus) {
        String sql = """
                UPDATE bookings
                SET booking_status = ?
                WHERE booking_id = ?
                """;

        jdbcTemplate.update(sql, bookingStatus, bookingId);
    }

    public void cancelBooking(int bookingId) {
        updateBookingStatus(bookingId, "CANCELLED");
    }
}
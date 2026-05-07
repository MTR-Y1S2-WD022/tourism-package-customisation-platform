package com.tourismplatform.dao;

import com.tourismplatform.model.Booking;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class BookingDAO {

    private final JdbcTemplate jdbcTemplate;

    public BookingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Booking> bookingRowMapper = (rs, rowNum) -> {
        Booking booking = new Booking();

        booking.setBookingId(rs.getInt("booking_id"));
        booking.setUserId(rs.getInt("user_id"));
        booking.setPackageId(rs.getInt("package_id"));
        booking.setStartDate(rs.getDate("start_date").toLocalDate());
        booking.setEndDate(rs.getDate("end_date").toLocalDate());
        booking.setHotelType(rs.getString("hotel_type"));
        booking.setMealOption(rs.getString("meal_option"));
        booking.setGuideOption(rs.getString("guide_option"));
        booking.setTotalAmount(rs.getBigDecimal("total_amount"));
        booking.setBookingStatus(rs.getString("booking_status"));
        booking.setPaymentStatus(rs.getString("payment_status"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            booking.setCreatedAt(createdAt.toLocalDateTime());
        }

        return booking;
    };

    public int saveBooking(Booking booking) {
        String sql = """
                INSERT INTO bookings
                (user_id, package_id, start_date, end_date, hotel_type, meal_option, guide_option,
                 total_amount, booking_status, payment_status)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(sql,
                booking.getUserId(),
                booking.getPackageId(),
                booking.getStartDate(),
                booking.getEndDate(),
                booking.getHotelType(),
                booking.getMealOption(),
                booking.getGuideOption(),
                booking.getTotalAmount(),
                booking.getBookingStatus(),
                booking.getPaymentStatus()
        );
    }

    public void saveBookingDestination(int bookingId, int destinationId) {
        String sql = """
                INSERT INTO booking_destinations
                (booking_id, destination_id)
                VALUES (?, ?)
                """;

        jdbcTemplate.update(sql, bookingId, destinationId);
    }

    public List<Booking> findAllBookings() {
        String sql = "SELECT * FROM bookings ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, bookingRowMapper);
    }

    public Booking findBookingById(int bookingId) {
        String sql = "SELECT * FROM bookings WHERE booking_id = ?";
        return jdbcTemplate.queryForObject(sql, bookingRowMapper, bookingId);
    }

    public List<Integer> findDestinationIdsByBookingId(int bookingId) {
        String sql = "SELECT destination_id FROM booking_destinations WHERE booking_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, bookingId);
    }

    public void cancelBooking(int bookingId) {
        String sql = """
                UPDATE bookings
                SET booking_status = 'CANCELLED'
                WHERE booking_id = ?
                """;

        jdbcTemplate.update(sql, bookingId);
    }
}
package com.tourismplatform.dao;

import com.tourismplatform.model.Review;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewDAO {

    private final JdbcTemplate jdbcTemplate;

    public ReviewDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Review> reviewRowMapper = (rs, rowNum) -> {
        Review review = new Review();

        review.setReviewId(rs.getInt("review_id"));
        review.setBookingId(rs.getInt("booking_id"));
        review.setUserId(rs.getInt("user_id"));
        review.setPackageId(rs.getInt("package_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        review.setStatus(rs.getString("status"));

        if (rs.getTimestamp("created_at") != null) {
            review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        return review;
    };

    public int addReview(Review review) {
        String sql = """
                INSERT INTO reviews (booking_id, user_id, package_id, rating, comment, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                review.getBookingId(),
                review.getUserId(),
                review.getPackageId(),
                review.getRating(),
                review.getComment(),
                review.getStatus()
        );
    }

    public List<Review> getAllVisibleReviews() {
        String sql = """
                SELECT *
                FROM reviews
                WHERE status = 'VISIBLE'
                ORDER BY created_at DESC
                """;

        return jdbcTemplate.query(sql, reviewRowMapper);
    }

    public List<Review> getAllReviewsForAdmin() {
        String sql = """
                SELECT *
                FROM reviews
                ORDER BY created_at DESC
                """;

        return jdbcTemplate.query(sql, reviewRowMapper);
    }

    public List<Review> getReviewsByUserId(int userId) {
        String sql = """
                SELECT *
                FROM reviews
                WHERE user_id = ?
                ORDER BY created_at DESC
                """;

        return jdbcTemplate.query(sql, reviewRowMapper, userId);
    }

    public Review getReviewById(int reviewId) {
        String sql = """
                SELECT *
                FROM reviews
                WHERE review_id = ?
                """;

        return jdbcTemplate.queryForObject(sql, reviewRowMapper, reviewId);
    }

    public Review getReviewByBookingId(int bookingId) {
        String sql = """
                SELECT *
                FROM reviews
                WHERE booking_id = ?
                """;

        return jdbcTemplate.queryForObject(sql, reviewRowMapper, bookingId);
    }

    public int updateReview(Review review) {
        String sql = """
                UPDATE reviews
                SET rating = ?, comment = ?
                WHERE review_id = ?
                """;

        return jdbcTemplate.update(
                sql,
                review.getRating(),
                review.getComment(),
                review.getReviewId()
        );
    }

    public int hideReview(int reviewId) {
        String sql = """
                UPDATE reviews
                SET status = 'HIDDEN'
                WHERE review_id = ?
                """;

        return jdbcTemplate.update(sql, reviewId);
    }

    public int showReview(int reviewId) {
        String sql = """
                UPDATE reviews
                SET status = 'VISIBLE'
                WHERE review_id = ?
                """;

        return jdbcTemplate.update(sql, reviewId);
    }

    public int deleteReview(int reviewId) {
        String sql = """
                DELETE FROM reviews
                WHERE review_id = ?
                """;

        return jdbcTemplate.update(sql, reviewId);
    }

    public boolean reviewExistsForBooking(int bookingId) {
        String sql = """
                SELECT COUNT(*)
                FROM reviews
                WHERE booking_id = ?
                """;

        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, bookingId);

        return count != null && count > 0;
    }
}

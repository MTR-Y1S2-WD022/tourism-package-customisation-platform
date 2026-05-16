package com.tourismplatform.dao;

import com.tourismplatform.model.Coupon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.tourismplatform.model.PercentageCoupon;
import com.tourismplatform.model.FixedCoupon;

import java.util.List;

@Repository
public class CouponDAO {

    private final JdbcTemplate jdbcTemplate;

    public CouponDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Coupon> couponRowMapper = (rs, rowNum) -> {

            String discountType = rs.getString("discount_type");

            Coupon coupon;

            if (discountType.equalsIgnoreCase("PERCENTAGE")) {

                coupon = new PercentageCoupon();

            } else {

                coupon = new FixedCoupon();
            }

        coupon.setCouponId(rs.getInt("coupon_id"));
        coupon.setCouponCode(rs.getString("coupon_code"));
        coupon.setDiscountType(rs.getString("discount_type"));
        coupon.setDiscountValue(rs.getDouble("discount_value"));
        coupon.setIssueDate(rs.getDate("issue_date").toLocalDate());
        coupon.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
        coupon.setStatus(rs.getString("status"));

        int adminId = rs.getInt("created_by_admin_id");
        if (rs.wasNull()) {
            coupon.setCreatedByAdminId(null);
        } else {
            coupon.setCreatedByAdminId(adminId);
        }

        if (rs.getTimestamp("created_at") != null) {
            coupon.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        return coupon;
    };

    public int saveCoupon(Coupon coupon) {
        String sql = """
                INSERT INTO coupons
                (coupon_code, discount_type, discount_value, issue_date, expiry_date, status, created_by_admin_id)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(sql,
                coupon.getCouponCode(),
                coupon.getDiscountType(),
                coupon.getDiscountValue(),
                coupon.getIssueDate(),
                coupon.getExpiryDate(),
                coupon.getStatus(),
                coupon.getCreatedByAdminId());
    }

    public List<Coupon> getAllCoupons() {
        String sql = "SELECT * FROM coupons ORDER BY coupon_id DESC";
        return jdbcTemplate.query(sql, couponRowMapper);
    }

    public Coupon getCouponById(int couponId) {
        String sql = "SELECT * FROM coupons WHERE coupon_id = ?";
        return jdbcTemplate.queryForObject(sql, couponRowMapper, couponId);
    }

    public Coupon getCouponByCode(String couponCode) {
        String sql = "SELECT * FROM coupons WHERE coupon_code = ?";
        return jdbcTemplate.queryForObject(sql, couponRowMapper, couponCode);
    }

    public int updateCoupon(Coupon coupon) {
        String sql = """
                UPDATE coupons
                SET coupon_code = ?,
                    discount_type = ?,
                    discount_value = ?,
                    issue_date = ?,
                    expiry_date = ?,
                    status = ?,
                    created_by_admin_id = ?
                WHERE coupon_id = ?
                """;

        return jdbcTemplate.update(sql,
                coupon.getCouponCode(),
                coupon.getDiscountType(),
                coupon.getDiscountValue(),
                coupon.getIssueDate(),
                coupon.getExpiryDate(),
                coupon.getStatus(),
                coupon.getCreatedByAdminId(),
                coupon.getCouponId());
    }

    public int extendExpiryDate(int couponId, java.time.LocalDate newExpiryDate) {
        String sql = "UPDATE coupons SET expiry_date = ? WHERE coupon_id = ?";
        return jdbcTemplate.update(sql, newExpiryDate, couponId);
    }

    public int cancelCoupon(int couponId) {
        String sql = "UPDATE coupons SET status = 'CANCELLED' WHERE coupon_id = ?";
        return jdbcTemplate.update(sql, couponId);
    }
}

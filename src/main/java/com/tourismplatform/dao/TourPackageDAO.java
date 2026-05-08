package com.tourismplatform.dao;

import com.tourismplatform.model.TourPackage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TourPackageDAO {

    private final JdbcTemplate jdbcTemplate;

    public TourPackageDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<TourPackage> tourPackageRowMapper = (rs, rowNum) -> {
        TourPackage tourPackage = new TourPackage();

        tourPackage.setPackageId(rs.getInt("package_id"));
        tourPackage.setPackageName(rs.getString("package_name"));
        tourPackage.setLocationArea(rs.getString("location_area"));
        tourPackage.setDescription(rs.getString("description"));
        tourPackage.setBasePrice(rs.getBigDecimal("base_price"));
        tourPackage.setImageUrl(rs.getString("image_url"));
        tourPackage.setStatus(rs.getString("status"));

        if (rs.getTimestamp("created_at") != null) {
            tourPackage.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        return tourPackage;
    };

    public int saveTourPackage(TourPackage tourPackage) {
        String sql = """
                INSERT INTO tour_packages
                (package_name, location_area, description, base_price, image_url, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                tourPackage.getPackageName(),
                tourPackage.getLocationArea(),
                tourPackage.getDescription(),
                tourPackage.getBasePrice(),
                tourPackage.getImageUrl(),
                "ACTIVE"
        );
    }

    public List<TourPackage> getAllActivePackages() {
        String sql = """
                SELECT * FROM tour_packages
                WHERE status = 'ACTIVE'
                ORDER BY package_id DESC
                """;

        return jdbcTemplate.query(sql, tourPackageRowMapper);
    }

    public TourPackage getTourPackageById(int packageId) {
        String sql = """
                SELECT * FROM tour_packages
                WHERE package_id = ?
                """;

        return jdbcTemplate.queryForObject(sql, tourPackageRowMapper, packageId);
    }

    public int updateTourPackage(TourPackage tourPackage) {
        String sql = """
                UPDATE tour_packages
                SET package_name = ?,
                    location_area = ?,
                    description = ?,
                    base_price = ?,
                    image_url = ?,
                    status = ?
                WHERE package_id = ?
                """;

        return jdbcTemplate.update(
                sql,
                tourPackage.getPackageName(),
                tourPackage.getLocationArea(),
                tourPackage.getDescription(),
                tourPackage.getBasePrice(),
                tourPackage.getImageUrl(),
                tourPackage.getStatus(),
                tourPackage.getPackageId()
        );
    }

    public int deactivateTourPackage(int packageId) {
        String sql = """
                UPDATE tour_packages
                SET status = 'INACTIVE'
                WHERE package_id = ?
                """;

        return jdbcTemplate.update(sql, packageId);
    }
}


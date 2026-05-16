package com.tourismplatform.dao;

import com.tourismplatform.model.Destination;

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

    public int activateTourPackage(int packageId) {
        String sql = """
            UPDATE tour_packages
            SET status = 'ACTIVE'
            WHERE package_id = ?
            """;

        return jdbcTemplate.update(sql, packageId);
    }

    public List<Integer> getAssignedDestinationIds(int packageId) {
        String sql = """
            SELECT destination_id
            FROM package_destinations
            WHERE package_id = ?
            """;

        return jdbcTemplate.queryForList(sql, Integer.class, packageId);
    }

    public int removeDestinationsFromPackage(int packageId) {
        String sql = """
            DELETE FROM package_destinations
            WHERE package_id = ?
            """;

        return jdbcTemplate.update(sql, packageId);
    }

    public int assignDestinationToPackage(int packageId, int destinationId) {
        String sql = """
            INSERT INTO package_destinations (package_id, destination_id)
            VALUES (?, ?)
            """;

        return jdbcTemplate.update(sql, packageId, destinationId);
    }

    public List<Destination> getDestinationsByPackageId(int packageId) {
        String sql = """
            SELECT d.*
            FROM destinations d
            INNER JOIN package_destinations pd
                ON d.destination_id = pd.destination_id
            WHERE pd.package_id = ?
            AND d.status = 'ACTIVE'
            ORDER BY d.destination_id DESC
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Destination destination = new Destination();

            destination.setDestinationId(rs.getInt("destination_id"));
            destination.setDestinationName(rs.getString("destination_name"));
            destination.setDescription(rs.getString("description"));
            destination.setImageUrl(rs.getString("image_url"));
            destination.setGoogleMapUrl(rs.getString("google_map_url"));
            destination.setBaseCost(rs.getBigDecimal("base_cost"));
            destination.setStatus(rs.getString("status"));

            if (rs.getTimestamp("created_at") != null) {
                destination.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            }

            return destination;
        }, packageId);
    }
    public int countDestinations(int packageId) {
        String sql = """
        SELECT COUNT(*)
        FROM package_destinations
        WHERE package_id = ?
    """;

        return jdbcTemplate.queryForObject(sql, Integer.class, packageId);
    }
}


package com.tourismplatform.dao;

import com.tourismplatform.model.Destination;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DestinationDAO {

    private final JdbcTemplate jdbcTemplate;

    public DestinationDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Destination> destinationRowMapper = (rs, rowNum) -> {
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
    };

    public int saveDestination(Destination destination) {
        String sql = """
                INSERT INTO destinations
                (destination_name, description, image_url, google_map_url, base_cost, status)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                destination.getDestinationName(),
                destination.getDescription(),
                destination.getImageUrl(),
                destination.getGoogleMapUrl(),
                destination.getBaseCost(),
                "ACTIVE"
        );
    }

    public List<Destination> getAllActiveDestinations() {
        String sql = """
            SELECT * FROM destinations
            ORDER BY destination_id DESC
            """;

        return jdbcTemplate.query(sql, destinationRowMapper);
    }
    
    public Destination getDestinationById(int destinationId) {
        String sql = """
                SELECT * FROM destinations
                WHERE destination_id = ?
                """;

        return jdbcTemplate.queryForObject(sql, destinationRowMapper, destinationId);
    }

    public int updateDestination(Destination destination) {
        String sql = """
                UPDATE destinations
                SET destination_name = ?,
                    description = ?,
                    image_url = ?,
                    google_map_url = ?,
                    base_cost = ?,
                    status = ?
                WHERE destination_id = ?
                """;

        return jdbcTemplate.update(
                sql,
                destination.getDestinationName(),
                destination.getDescription(),
                destination.getImageUrl(),
                destination.getGoogleMapUrl(),
                destination.getBaseCost(),
                destination.getStatus(),
                destination.getDestinationId()
        );
    }

    public int deactivateDestination(int destinationId) {
        String sql = """
                UPDATE destinations
                SET status = 'INACTIVE'
                WHERE destination_id = ?
                """;

        return jdbcTemplate.update(sql, destinationId);
    }
}


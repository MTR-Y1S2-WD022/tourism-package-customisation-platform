package com.tourismplatform.dao;

import com.tourismplatform.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {

    private final JdbcTemplate jdbcTemplate;

    public UserDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<User> userRowMapper = (rs, rowNum) -> {
        User user = new User();

        user.setUserId(rs.getInt("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setAddress(rs.getString("address"));
        user.setProfileImage(rs.getString("profile_image"));
        user.setStatus(rs.getString("status"));

        if (rs.getTimestamp("created_at") != null) {
            user.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        return user;
    };

    public User findByEmailAndPassword(String email, String password) {
        String sql = """
                SELECT * FROM users
                WHERE email = ?
                AND password = ?
                AND status = 'ACTIVE'
                """;

        List<User> users = jdbcTemplate.query(sql, userRowMapper, email, password);

        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }

    public List<User> findAll() {
        String sql = """
                SELECT * FROM users
                ORDER BY user_id DESC
                """;

        return jdbcTemplate.query(sql, userRowMapper);
    }

    public User findById(int userId) {
        String sql = """
                SELECT * FROM users
                WHERE user_id = ?
                """;

        List<User> users = jdbcTemplate.query(sql, userRowMapper, userId);

        if (users.isEmpty()) {
            return null;
        }

        return users.get(0);
    }

    public int save(User user) {
        String sql = """
                INSERT INTO users (full_name, email, password, phone_number, address, profile_image, status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getProfileImage(),
                user.getStatus()
        );
    }

    public int update(User user) {
        String sql = """
                UPDATE users
                SET full_name = ?,
                    email = ?,
                    password = ?,
                    phone_number = ?,
                    address = ?,
                    profile_image = ?,
                    status = ?
                WHERE user_id = ?
                """;

        return jdbcTemplate.update(
                sql,
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.getAddress(),
                user.getProfileImage(),
                user.getStatus(),
                user.getUserId()
        );
    }

    public int deactivate(int userId) {
        String sql = "UPDATE users SET status = 'INACTIVE' WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }

    public int activate(int userId) {
        String sql = "UPDATE users SET status = 'ACTIVE' WHERE user_id = ?";
        return jdbcTemplate.update(sql, userId);
    }


}
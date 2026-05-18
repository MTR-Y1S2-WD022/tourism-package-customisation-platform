package com.tourismplatform.dao;

import com.tourismplatform.model.Admin;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminDAO {

    private final JdbcTemplate jdbcTemplate;

    public AdminDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Admin> adminRowMapper = (rs, rowNum) -> {
        Admin admin = new Admin();

        admin.setAdminId(rs.getInt("admin_id"));
        admin.setFullName(rs.getString("full_name"));
        admin.setEmail(rs.getString("email"));
        admin.setPassword(rs.getString("password"));
        admin.setRole(rs.getString("role"));
        admin.setDefault(rs.getBoolean("is_default"));

        if (rs.getTimestamp("created_at") != null) {
            admin.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        }

        return admin;
    };

    public Admin findByEmailAndPassword(String email, String password) {
        String sql = """
                SELECT * FROM admins
                WHERE email = ?
                AND password = ?
                """;

        List<Admin> admins = jdbcTemplate.query(sql, adminRowMapper, email, password);

        if (admins.isEmpty()) {
            return null;
        }

        return admins.get(0);
    }

    public List<Admin> findAll() {
        String sql = """
                SELECT * FROM admins
                ORDER BY admin_id ASC
                """;

        return jdbcTemplate.query(sql, adminRowMapper);
    }

    public Admin findById(int adminId) {
        String sql = """
                SELECT * FROM admins
                WHERE admin_id = ?
                """;

        List<Admin> admins = jdbcTemplate.query(sql, adminRowMapper, adminId);

        if (admins.isEmpty()) {
            return null;
        }

        return admins.get(0);
    }

    public int save(Admin admin) {
        String sql = """
                INSERT INTO admins (full_name, email, password, role, is_default)
                VALUES (?, ?, ?, ?, ?)
                """;

        return jdbcTemplate.update(
                sql,
                admin.getFullName(),
                admin.getEmail(),
                admin.getPassword(),
                admin.getRole(),
                admin.isDefault()
        );
    }

    public int update(Admin admin) {
        String sql = """
                UPDATE admins
                SET full_name = ?,
                    email = ?,
                    password = ?,
                    role = ?
                WHERE admin_id = ?
                """;

        return jdbcTemplate.update(
                sql,
                admin.getFullName(),
                admin.getEmail(),
                admin.getPassword(),
                admin.getRole(),
                admin.getAdminId()
        );
    }

    public int deleteAdmin(int adminId) {
        String sql = "DELETE FROM admins WHERE admin_id = ?";
        jdbcTemplate.update(sql, adminId);
        return adminId;
    }


}
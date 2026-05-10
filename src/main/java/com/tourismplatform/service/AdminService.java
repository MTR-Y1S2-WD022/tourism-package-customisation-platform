package com.tourismplatform.service;

import com.tourismplatform.dao.AdminDAO;
import com.tourismplatform.model.Admin;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final AdminDAO adminDAO;

    public AdminService(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    public Admin login(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            return null;
        }

        return adminDAO.findByEmailAndPassword(email.trim(), password.trim());
    }

    public List<Admin> getAllAdmins() {
        return adminDAO.findAll();
    }

    public Admin getAdminById(int adminId) {
        return adminDAO.findById(adminId);
    }

    public boolean saveAdmin(Admin admin) {
        prepareAdminBeforeSave(admin);
        int result = adminDAO.save(admin);
        return result > 0;
    }

    public boolean updateAdmin(Admin admin) {
        prepareAdminBeforeUpdate(admin);
        int result = adminDAO.update(admin);
        return result > 0;
    }

    public String deactivateAdmin(int adminIdToDeactivate, int loggedInAdminId) {
        Admin adminToDeactivate = adminDAO.findById(adminIdToDeactivate);

        if (adminToDeactivate == null) {
            return "Admin not found.";
        }

        if (adminToDeactivate.isDefault()) {
            return "Default admin cannot be deactivated.";
        }

        if (adminIdToDeactivate == loggedInAdminId) {
            return "You cannot deactivate your own account.";
        }

        int result = adminDAO.deactivate(adminIdToDeactivate);

        if (result > 0) {
            return "Admin deactivated successfully.";
        }

        return "Failed to deactivate admin.";
    }

    private void prepareAdminBeforeSave(Admin admin) {
        if (admin.getRole() == null || admin.getRole().trim().isEmpty()) {
            admin.setRole("ADMIN");
        }

        if (admin.getStatus() == null || admin.getStatus().trim().isEmpty()) {
            admin.setStatus("ACTIVE");
        }

        admin.setDefault(false);
    }

    private void prepareAdminBeforeUpdate(Admin admin) {
        if (admin.getRole() == null || admin.getRole().trim().isEmpty()) {
            admin.setRole("ADMIN");
        }

        if (admin.getStatus() == null || admin.getStatus().trim().isEmpty()) {
            admin.setStatus("ACTIVE");
        }
    }

    public String validateAdmin(Admin admin) {
        if (admin == null) {
            return "Admin details are missing.";
        }

        if (isBlank(admin.getFullName())) {
            return "Full name is required.";
        }

        if (isBlank(admin.getEmail())) {
            return "Email is required.";
        }

        if (!admin.getEmail().contains("@")) {
            return "Please enter a valid email address.";
        }

        if (isBlank(admin.getPassword())) {
            return "Password is required.";
        }

        if (admin.getPassword().length() < 8) {
            return "Password must have at least 8 characters.";
        }

        if (isBlank(admin.getRole())) {
            return "Role is required.";
        }

        if (!admin.getRole().equals("ADMIN") && !admin.getRole().equals("SUPER_ADMIN")) {
            return "Role must be ADMIN or SUPER_ADMIN.";
        }

        if (isBlank(admin.getStatus())) {
            return "Status is required.";
        }

        if (!admin.getStatus().equals("ACTIVE") && !admin.getStatus().equals("INACTIVE")) {
            return "Status must be ACTIVE or INACTIVE.";
        }

        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}
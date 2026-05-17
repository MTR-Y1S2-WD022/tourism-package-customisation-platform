package com.tourismplatform.service;

import com.tourismplatform.dao.AdminDAO;
import com.tourismplatform.model.Admin;
import org.springframework.beans.factory.annotation.Autowired;
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

    public String deactivateAdmin(int targetAdminId, int loggedInAdminId) {
        Admin targetAdmin = adminDAO.findById(targetAdminId);
        Admin loggedInAdmin = adminDAO.findById(loggedInAdminId);

        if (targetAdmin == null) {
            return "Admin not found.";
        }

        if (loggedInAdmin == null) {
            return "Logged-in admin not found.";
        }

        if (!canManageAdmins(loggedInAdmin)) {
            return "You do not have permission to deactivate admin accounts.";
        }

        if (targetAdmin.getAdminId() == loggedInAdmin.getAdminId()) {
            return "You cannot deactivate your own account.";
        }

        if (targetAdmin.isDefault()) {
            return "Default admin cannot be deactivated.";
        }

        int result = adminDAO.deactivate(targetAdminId);

        if (result > 0) {
            return "Admin deactivated successfully.";
        }

        return "Failed to deactivate admin.";
    }

    public String activateAdmin(int targetAdminId, int loggedInAdminId) {
        Admin targetAdmin = adminDAO.findById(targetAdminId);
        Admin loggedInAdmin = adminDAO.findById(loggedInAdminId);

        if (targetAdmin == null) {
            return "Admin not found.";
        }

        if (loggedInAdmin == null) {
            return "Logged-in admin not found.";
        }

        if (!canManageAdmins(loggedInAdmin)) {
            return "You do not have permission to activate admin accounts.";
        }

        if (targetAdmin.isDefault()) {
            return "Default admin is already protected as ACTIVE.";
        }

        int result = adminDAO.activate(targetAdminId);

        if (result > 0) {
            return "Admin activated successfully.";
        }

        return "Failed to activate admin.";
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

    public boolean canManageAdmins(Admin loggedInAdmin) {
        if (loggedInAdmin == null) {
            return false;
        }

        return loggedInAdmin.isDefault() || "SUPER_ADMIN".equals(loggedInAdmin.getRole());
    }

    public boolean canEditAdmin(Admin targetAdmin, Admin loggedInAdmin) {
        if (targetAdmin == null || loggedInAdmin == null) {
            return false;
        }

        if (targetAdmin.isDefault()) {
            return loggedInAdmin.isDefault()
                    && targetAdmin.getAdminId() == loggedInAdmin.getAdminId();
        }

        return canManageAdmins(loggedInAdmin);
    }



}
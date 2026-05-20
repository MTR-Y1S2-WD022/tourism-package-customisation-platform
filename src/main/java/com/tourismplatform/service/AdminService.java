package com.tourismplatform.service;

import com.tourismplatform.dao.AdminDAO;
import com.tourismplatform.model.Admin;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService implements AccountOperations<Admin> {

    private final AdminDAO adminDAO;

    public AdminService(AdminDAO adminDAO) {
        this.adminDAO = adminDAO;
    }

    // 1. RUNTIME POLYMORPHISM (OVERRIDING INTERFACE METHODS)

    @Override
    public Admin login(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }
        return adminDAO.findByEmailAndPassword(email.trim(), password.trim());
    }

    @Override
    public boolean update(Admin admin) {
        prepareAdminBeforeUpdate(admin);
        return adminDAO.update(admin) > 0;
    }

    @Override
    public void logout(HttpSession session) {
        session.removeAttribute("loggedInAdmin");
        session.invalidate();
    }

    @Override
    public String validate(Admin admin) {
        if (admin.getFullName() == null || admin.getFullName().trim().isEmpty()) return "Full name is required.";
        if (admin.getEmail() == null || admin.getEmail().trim().isEmpty()) return "Email is required.";
        if (!admin.getEmail().contains("@")) return "Please enter a valid email address.";
        if (admin.getPassword() == null || admin.getPassword().trim().isEmpty()) return "Password is required.";
        if (admin.getPassword().length() < 8) return "Password must have at least 8 characters.";
        return null;
    }


    // OTHER METHODS

    public List<Admin> getAllAdmins() {
        return adminDAO.findAll();
    }

    public Admin getAdminById(int adminId) {
        return adminDAO.findById(adminId);
    }

    public boolean saveAdmin(Admin admin) {
        prepareAdminBeforeSave(admin);
        try {
            return adminDAO.save(admin) > 0;
        } catch (Exception e) {
            System.out.println("Database Error saving admin: " + e.getMessage());
            return false;
        }
    }

    public String deleteAdmin(int targetAdminId, int loggedInAdminId) {
        Admin target = adminDAO.findById(targetAdminId);
        Admin logged = adminDAO.findById(loggedInAdminId);

        if (target == null) return "Admin not found.";
        if (logged == null) return "Logged-in admin not found.";
        if (target.isDefault()) return "Default admin cannot be deleted.";
        if (target.getAdminId() == logged.getAdminId()) return "You cannot delete your own account.";

        int result = adminDAO.deleteAdmin(targetAdminId);
        return result > 0 ? "Admin deleted successfully." : "Failed to delete admin.";
    }

    private void prepareAdminBeforeSave(Admin admin) {
        admin.setRole("ADMIN");
        admin.setDefault(false);
    }

    private void prepareAdminBeforeUpdate(Admin admin) {
        admin.setRole("ADMIN");
    }

    public boolean canManageAdmins(Admin loggedInAdmin) {
        if (loggedInAdmin == null) return false;
        return loggedInAdmin.isDefault() || "SUPER_ADMIN".equals(loggedInAdmin.getRole());
    }

    public boolean canEditAdmin(Admin targetAdmin, Admin loggedInAdmin) {
        if (targetAdmin == null || loggedInAdmin == null) return false;
        if (targetAdmin.isDefault()) {
            return loggedInAdmin.isDefault() && targetAdmin.getAdminId() == loggedInAdmin.getAdminId();
        }
        return canManageAdmins(loggedInAdmin);
    }
}
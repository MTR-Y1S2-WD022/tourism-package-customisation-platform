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

    public String deleteAdmin(int targetAdminId, int loggedInAdminId) {

        Admin target = adminDAO.findById(targetAdminId);
        Admin logged = adminDAO.findById(loggedInAdminId);

        if (target == null) {
            return "Admin not found.";
        }

        if (logged == null) {
            return "Logged-in admin not found.";
        }

        if (target.isDefault()) {
            return "Default admin cannot be deleted.";
        }

        if (target.getAdminId() == logged.getAdminId()) {
            return "You cannot delete your own account.";
        }

        int result = adminDAO.deleteAdmin(targetAdminId);

        return result > 0 ? "Admin deleted successfully." : "Failed to delete admin.";
    }



    private void prepareAdminBeforeSave(Admin admin) {
        if (admin.getRole() == null || admin.getRole().trim().isEmpty()) {
            admin.setRole("ADMIN");
        }
        admin.setDefault(false);
    }

    private void prepareAdminBeforeUpdate(Admin admin) {
        if (admin.getRole() == null || admin.getRole().trim().isEmpty()) {
            admin.setRole("ADMIN");
        }
    }

    public String validateAdmin(Admin admin) {
        return admin.validate();
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
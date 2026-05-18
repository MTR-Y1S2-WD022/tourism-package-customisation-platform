package com.tourismplatform.model;

import java.time.LocalDateTime;

public class Admin extends Person {

    private int adminId;
    private String role;
    private boolean isDefault;

    public Admin() {
    }

    public Admin(int adminId, String fullName, String email, String password,
                 String role, String status, boolean isDefault, LocalDateTime createdAt) {
        super(fullName, email, password, status, createdAt);
        this.adminId = adminId;
        this.role = role;
        this.isDefault = isDefault;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String getDashboardPath() {
        return "/admin/dashboard";
    }

    @Override
    public String validate() {
        String base = super.validate();
        if (base != null) return base;

        if (role == null || role.trim().isEmpty()) {
            return "Role is required.";
        }

        if (!role.equals("ADMIN") && !role.equals("SUPER_ADMIN")) {
            return "Role must be ADMIN or SUPER_ADMIN.";
        }

        return null;
    }


}
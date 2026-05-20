package com.tourismplatform.controller;

import com.tourismplatform.model.Admin;
import com.tourismplatform.model.User;
import com.tourismplatform.service.AdminService;
import com.tourismplatform.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final UserService userService;

    public AdminController(AdminService adminService, UserService userService) {
        this.adminService = adminService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "admin/admin-login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        Admin admin = adminService.login(email, password);

        if (admin == null) {
            model.addAttribute("errorMessage", "Invalid email, password, or inactive admin account.");
            return "admin/admin-login";
        }

        session.setAttribute("loggedInAdmin", admin);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        adminService.logout(session);
        return "redirect:/admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", loggedInAdmin);
        return "admin/dashboard";
    }

    private Admin getLoggedInAdmin(HttpSession session) {
        Object adminObject = session.getAttribute("loggedInAdmin");
        return (adminObject instanceof Admin) ? (Admin) adminObject : null;
    }

    @GetMapping("/list")
    public String listAdmins(HttpSession session,
                             Model model,
                             @RequestParam(required = false) String message) {

        Admin loggedInAdmin = getLoggedInAdmin(session);
        if (loggedInAdmin == null) return "redirect:/admin/login";

        List<Admin> admins = adminService.getAllAdmins();
        model.addAttribute("admins", admins);
        model.addAttribute("loggedInAdmin", loggedInAdmin);
        model.addAttribute("loggedInAdminId", loggedInAdmin.getAdminId());
        model.addAttribute("message", message);

        return "admin/admin-list";
    }

    @GetMapping("/edit/{adminId}")
    public String showEditAdminForm(@PathVariable int adminId, HttpSession session, Model model) {

        Admin loggedInAdmin = getLoggedInAdmin(session);
        if (loggedInAdmin == null) return "redirect:/admin/login";

        Admin admin = adminService.getAdminById(adminId);
        if (admin == null) return "redirect:/admin/list?message=Admin not found.";

        if (!adminService.canEditAdmin(admin, loggedInAdmin)) {
            return "redirect:/admin/list?message=You do not have permission to edit this admin.";
        }

        boolean canEditRole = !admin.isDefault() && adminService.canManageAdmins(loggedInAdmin);

        model.addAttribute("admin", admin);
        model.addAttribute("formTitle", "Edit Admin");
        model.addAttribute("formAction", "/admin/update");
        model.addAttribute("canEditRole", canEditRole);

        return "admin/admin-form";
    }

    @PostMapping("/save")
    public String saveAdmin(@ModelAttribute Admin admin, HttpSession session, Model model) {

        Admin loggedInAdmin = getLoggedInAdmin(session);
        if (loggedInAdmin == null) return "redirect:/admin/login";

        if (admin.getRole() == null || admin.getRole().trim().isEmpty()) {
            admin.setRole("ADMIN");
        }

        String validationMessage = adminService.validate(admin);

        if (validationMessage != null) {
            model.addAttribute("errorMessage", validationMessage);
            model.addAttribute("admin", admin);
            model.addAttribute("formTitle", "Add New Admin");
            model.addAttribute("formAction", "/admin/save");
            return "admin/admin-form";
        }

        boolean saved = adminService.saveAdmin(admin);

        if (!saved) {
            model.addAttribute("errorMessage", "Failed to save admin. Email might already exist.");
            model.addAttribute("admin", admin);
            model.addAttribute("formTitle", "Add New Admin");
            model.addAttribute("formAction", "/admin/save");
            return "admin/admin-form";
        }

        return "redirect:/admin/list?message=Admin saved successfully.";
    }

    @GetMapping("/new")
    public String showAddAdminForm(HttpSession session, Model model) {
        Admin loggedInAdmin = getLoggedInAdmin(session);
        if (loggedInAdmin == null) return "redirect:/admin/login";

        if (!adminService.canManageAdmins(loggedInAdmin)) {
            return "redirect:/admin/list?message=You do not have permission to add admins.";
        }

        Admin admin = new Admin();
        admin.setRole("ADMIN");

        model.addAttribute("admin", admin);
        model.addAttribute("formTitle", "Add New Admin");
        model.addAttribute("formAction", "/admin/save");
        model.addAttribute("canEditRole", true);

        return "admin/admin-form";
    }

    @PostMapping("/update")
    public String updateAdmin(@ModelAttribute Admin admin, HttpSession session, Model model) {

        Admin loggedInAdmin = getLoggedInAdmin(session);
        if (loggedInAdmin == null) return "redirect:/admin/login";

        Admin existingAdmin = adminService.getAdminById(admin.getAdminId());
        if (existingAdmin == null) return "redirect:/admin/list?message=Admin not found.";

        if (!adminService.canEditAdmin(existingAdmin, loggedInAdmin)) {
            return "redirect:/admin/list?message=You do not have permission to update this admin.";
        }

        if (existingAdmin.isDefault()) {
            admin.setRole("SUPER_ADMIN");
            admin.setDefault(true);
        }

        if (existingAdmin.isDefault() || !adminService.canManageAdmins(loggedInAdmin)) {
            admin.setRole(existingAdmin.getRole());
        }

        String validationMessage = adminService.validate(admin);

        if (validationMessage != null) {
            model.addAttribute("errorMessage", validationMessage);
            model.addAttribute("admin", admin);
            model.addAttribute("formTitle", "Edit Admin");
            model.addAttribute("formAction", "/admin/update");
            model.addAttribute("canEditRole", !existingAdmin.isDefault() && adminService.canManageAdmins(loggedInAdmin));
            return "admin/admin-form";
        }

        boolean updated = adminService.update(admin);

        if (!updated) {
            model.addAttribute("errorMessage", "Failed to update admin.");
            model.addAttribute("admin", admin);
            model.addAttribute("formTitle", "Edit Admin");
            model.addAttribute("formAction", "/admin/update");
            model.addAttribute("canEditRole", !existingAdmin.isDefault() && adminService.canManageAdmins(loggedInAdmin));
            return "admin/admin-form";
        }

        return "redirect:/admin/list?message=Admin updated successfully.";
    }

    @GetMapping("/delete/{adminId}")
    public String deleteAdmin(@PathVariable int adminId, HttpSession session) {
        Admin loggedInAdmin = getLoggedInAdmin(session);
        if (loggedInAdmin == null) return "redirect:/admin/login";

        String message = adminService.deleteAdmin(adminId, loggedInAdmin.getAdminId());
        return "redirect:/admin/list?message=" + message;
    }

    @GetMapping("/users")
    public String listUsers(HttpSession session, Model model, @RequestParam(required = false) String message) {
        Admin loggedInAdmin = getLoggedInAdmin(session);
        if (loggedInAdmin == null) return "redirect:/admin/login";

        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("message", message);

        return "admin/user-list";
    }

    @GetMapping("/users/edit/{id}")
    public String showEditUserForm(@PathVariable("id") int id, HttpSession session, Model model) {
        // Security Check: Is admin logged in?
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/admin/login";

        // Fetch the user from the database
        User user = userService.getUserById(id);

        if (user == null) {
            return "redirect:/admin/users?error=User not found";
        }

        model.addAttribute("user", user);
        return "admin/user-edit"; // This tells Spring to load admin/user-edit.html
    }

    // 2. Save the Updates
    @PostMapping("/users/update")
    public String updateUserByAdmin(@ModelAttribute User user, HttpSession session, Model model) {
        // Security Check
        Admin loggedInAdmin = (Admin) session.getAttribute("loggedInAdmin");
        if (loggedInAdmin == null) return "redirect:/admin/login";

        // If the admin left the password blank, keep the old password
        User existingUser = userService.getUserById(user.getUserId());
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        }

        // Use your AccountOperations overridden update method
        boolean updated = userService.update(user);

        if (!updated) {
            model.addAttribute("errorMessage", "Failed to update user details.");
            model.addAttribute("user", user);
            return "admin/user-edit";
        }

        return "redirect:/admin/users?message=User updated successfully.";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable("id") int id, HttpSession session) {
        // Security Check
        Admin loggedInAdmin = getLoggedInAdmin(session);
        if (loggedInAdmin == null) return "redirect:/admin/login";


        userService.deleteUser(id);

        return "redirect:/admin/users?message=User successfully deleted.";
    }

}
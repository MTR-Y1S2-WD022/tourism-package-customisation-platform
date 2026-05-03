package com.tourismplatform.controller;

import com.tourismplatform.model.Admin;
import com.tourismplatform.service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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
        session.setAttribute("loggedInAdminId", admin.getAdminId());

        return "redirect:/admin/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/admin/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        Admin loggedInAdmin = getLoggedInAdmin(session);

        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }

        model.addAttribute("admin", loggedInAdmin);
        return "admin/dashboard";
    }

    @GetMapping("/list")
    public String listAdmins(HttpSession session,
                             Model model,
                             @RequestParam(required = false) String message) {

        Admin loggedInAdmin = getLoggedInAdmin(session);

        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }

        List<Admin> admins = adminService.getAllAdmins();

        model.addAttribute("admins", admins);
        model.addAttribute("loggedInAdminId", loggedInAdmin.getAdminId());
        model.addAttribute("message", message);

        return "admin/admin-list";
    }

    @GetMapping("/new")
    public String showAddAdminForm(HttpSession session, Model model) {
        Admin loggedInAdmin = getLoggedInAdmin(session);

        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }

        Admin admin = new Admin();
        admin.setRole("ADMIN");
        admin.setStatus("ACTIVE");

        model.addAttribute("admin", admin);
        model.addAttribute("formTitle", "Add New Admin");
        model.addAttribute("formAction", "/admin/save");

        return "admin/admin-form";
    }

    @PostMapping("/save")
    public String saveAdmin(@ModelAttribute Admin admin,
                            HttpSession session,
                            Model model) {

        Admin loggedInAdmin = getLoggedInAdmin(session);

        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }

        boolean saved = adminService.saveAdmin(admin);

        if (!saved) {
            model.addAttribute("errorMessage", "Failed to save admin.");
            model.addAttribute("admin", admin);
            model.addAttribute("formTitle", "Add New Admin");
            model.addAttribute("formAction", "/admin/save");
            return "admin/admin-form";
        }

        return "redirect:/admin/list?message=Admin saved successfully.";
    }

    @GetMapping("/edit/{adminId}")
    public String showEditAdminForm(@PathVariable int adminId,
                                    HttpSession session,
                                    Model model) {

        Admin loggedInAdmin = getLoggedInAdmin(session);

        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }

        Admin admin = adminService.getAdminById(adminId);

        if (admin == null) {
            return "redirect:/admin/list?message=Admin not found.";
        }

        model.addAttribute("admin", admin);
        model.addAttribute("formTitle", "Edit Admin");
        model.addAttribute("formAction", "/admin/update");

        return "admin/admin-form";
    }

    @PostMapping("/update")
    public String updateAdmin(@ModelAttribute Admin admin,
                              HttpSession session,
                              Model model) {

        Admin loggedInAdmin = getLoggedInAdmin(session);

        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }

        boolean updated = adminService.updateAdmin(admin);

        if (!updated) {
            model.addAttribute("errorMessage", "Failed to update admin.");
            model.addAttribute("admin", admin);
            model.addAttribute("formTitle", "Edit Admin");
            model.addAttribute("formAction", "/admin/update");
            return "admin/admin-form";
        }

        return "redirect:/admin/list?message=Admin updated successfully.";
    }

    @GetMapping("/delete/{adminId}")
    public String deactivateAdmin(@PathVariable int adminId,
                                  HttpSession session) {

        Admin loggedInAdmin = getLoggedInAdmin(session);

        if (loggedInAdmin == null) {
            return "redirect:/admin/login";
        }

        String message = adminService.deactivateAdmin(adminId, loggedInAdmin.getAdminId());

        return "redirect:/admin/list?message=" + message;
    }

    private Admin getLoggedInAdmin(HttpSession session) {
        Object adminObject = session.getAttribute("loggedInAdmin");

        if (adminObject == null) {
            return null;
        }

        return (Admin) adminObject;
    }
}
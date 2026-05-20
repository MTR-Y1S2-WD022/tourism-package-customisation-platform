package com.tourismplatform.controller;

import com.tourismplatform.model.User;
import com.tourismplatform.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {

        // 1. NEW LOGIC: Check if email already exists
        if (userService.emailExists(user.getEmail())) {
            model.addAttribute("errorMessage", "This email is already registered. Please log in or use a different email.");
            model.addAttribute("user", user); // This keeps what they typed so they don't have to start over!
            return "user/register";
        }

        // 2. Normal Validation
        String validationMessage = userService.validate(user);
        if (validationMessage != null) {
            model.addAttribute("errorMessage", validationMessage);
            model.addAttribute("user", user);
            return "user/register";
        }

        // 3. Save User
        boolean registered = userService.registerUser(user);
        if (!registered) {
            model.addAttribute("errorMessage", "System error: Failed to register user.");
            model.addAttribute("user", user);
            return "user/register";
        }

        return "redirect:/login?message=Registration successful. Please login.";
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(required = false) String message, Model model) {
        model.addAttribute("message", message);
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userService.login(email, password);

        if (user == null) {
            model.addAttribute("errorMessage", "Invalid email, password, or inactive user account.");
            return "user/login";
        }

        session.setAttribute("loggedInUser", user);
        session.setAttribute("loggedInUserId", user.getUserId());

        return "redirect:/user/dashboard";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        userService.logout(session);
        return "redirect:/login?message=Logged out successfully.";
    }

    @GetMapping("/user/profile")
    public String showProfile(HttpSession session, Model model) {
        User loggedInUser = getLoggedInUser(session);
        if (loggedInUser == null) return "redirect:/login";

        User latestUser = userService.getUserById(loggedInUser.getUserId());
        model.addAttribute("user", latestUser);
        return "user/profile";
    }

    @PostMapping("/user/update")
    public String updateProfile(@ModelAttribute User user,
                                HttpSession session,
                                Model model) { // Removed @RequestParam MultipartFile

        User loggedInUser = getLoggedInUser(session);
        if (loggedInUser == null) return "redirect:/login";

        User existingUser = userService.getUserById(loggedInUser.getUserId());

        user.setUserId(existingUser.getUserId());
        user.setEmail(existingUser.getEmail());
        user.setPassword(existingUser.getPassword());

        boolean updated = userService.update(user);

        if (!updated) {
            model.addAttribute("errorMessage", "Failed to update profile.");
            model.addAttribute("user", existingUser);
            return "user/profile";
        }

        session.setAttribute("loggedInUser", userService.getUserById(user.getUserId()));
        return "redirect:/user/profile?message=Profile updated successfully.";
    }


    @PostMapping("/user/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 HttpSession session,
                                 Model model) {

        User loggedInUser = getLoggedInUser(session);
        if (loggedInUser == null) return "redirect:/login";

        User existingUser = userService.getUserById(loggedInUser.getUserId());

        if (!existingUser.getPassword().equals(oldPassword)) {
            model.addAttribute("errorMessage", "Incorrect current password.");
            model.addAttribute("user", existingUser);
            return "user/profile";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("errorMessage", "New passwords do not match.");
            model.addAttribute("user", existingUser);
            return "user/profile";
        }

        if (newPassword.length() < 8) {
            model.addAttribute("errorMessage", "New password must be at least 8 characters.");
            model.addAttribute("user", existingUser);
            return "user/profile";
        }

        existingUser.setPassword(newPassword);
        userService.update(existingUser); // Polymorphic update call

        return "redirect:/user/profile?message=Password changed successfully.";
    }

    @GetMapping("/user/dashboard")
    public String showUserDashboard(HttpSession session, Model model) {
        User user = getLoggedInUser(session);
        if (user == null) return "redirect:/login";
        model.addAttribute("user", user);
        return "user/dashboard";
    }

    private User getLoggedInUser(HttpSession session) {
        Object userObject = session.getAttribute("loggedInUser");
        return (userObject instanceof User) ? (User) userObject : null;
    }
}
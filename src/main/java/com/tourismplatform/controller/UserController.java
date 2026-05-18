package com.tourismplatform.controller;

import com.tourismplatform.model.Person;
import com.tourismplatform.model.User;
import com.tourismplatform.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);

        return "user/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, Model model) {

        String validationMessage = userService.validateUser(user);

        if (validationMessage != null) {
            model.addAttribute("errorMessage", validationMessage);
            model.addAttribute("user", user);
            return "user/register";
        }

        boolean registered = userService.registerUser(user);

        if (!registered) {
            model.addAttribute("errorMessage", "Failed to register user.");
            model.addAttribute("user", user);
            return "user/register";
        }

        return "redirect:/login?message=Registration successful. Please login.";
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(required = false) String message,
                                Model model) {
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
        
        Person person = user;

        session.setAttribute("loggedInUser", person);
        session.setAttribute("loggedInUserId", user.getUserId());


        String dashboard = person.getDashboardPath();

        return "redirect:" + dashboard;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?message=Logged out successfully.";
    }

    @GetMapping("/user/profile")
    public String showProfile(HttpSession session, Model model) {
        User loggedInUser = getLoggedInUser(session);

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        User latestUser = userService.getUserById(loggedInUser.getUserId());

        if (latestUser == null) {
            session.invalidate();
            return "redirect:/login?message=User account not found.";
        }

        model.addAttribute("user", latestUser);

        return "user/profile";
    }

    @PostMapping("/user/update")
    public String updateProfile(@ModelAttribute User user,
                                @RequestParam("profileImageFile") MultipartFile file,
                                HttpSession session,
                                Model model) {

        User loggedInUser = getLoggedInUser(session);

        if (loggedInUser == null) {
            return "redirect:/login";
        }

        user.setUserId(loggedInUser.getUserId());

        // IMAGE HANDLING
        if (!file.isEmpty()) {
            try {
                String fileName = file.getOriginalFilename();

                String uploadDir = "src/main/resources/static/images/";

                File saveFile = new File(uploadDir + fileName);
                file.transferTo(saveFile);

                user.setProfileImage(fileName);

            } catch (Exception e) {
                model.addAttribute("errorMessage", "Image upload failed");
                return "user/profile";
            }
        } else {
            user.setProfileImage(loggedInUser.getProfileImage());
        }

        boolean updated = userService.updateUser(user);

        if (!updated) {
            model.addAttribute("errorMessage", "Failed to update profile.");
            return "user/profile";
        }

        User updatedUser = userService.getUserById(user.getUserId());
        session.setAttribute("loggedInUser", updatedUser);

        return "redirect:/user/profile?message=Profile updated successfully.";
    }

    @GetMapping("/user/dashboard")
    public String showUserDashboard(HttpSession session, Model model) {

        User user = getLoggedInUser(session);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);

        return "user/dashboard";
    }

    private User getLoggedInUser(HttpSession session) {
        Object userObject = session.getAttribute("loggedInUser");

        if (userObject == null) {
            return null;
        }

        return (User) userObject;
    }
}
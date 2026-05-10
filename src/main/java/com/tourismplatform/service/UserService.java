package com.tourismplatform.service;

import com.tourismplatform.dao.UserDAO;
import com.tourismplatform.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public User login(String email, String password) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }

        if (password == null || password.trim().isEmpty()) {
            return null;
        }

        return userDAO.findByEmailAndPassword(email.trim(), password.trim());
    }

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }

    public boolean registerUser(User user) {
        prepareUserBeforeSave(user);
        int result = userDAO.save(user);
        return result > 0;
    }

    public boolean updateUser(User user) {
        prepareUserBeforeUpdate(user);
        int result = userDAO.update(user);
        return result > 0;
    }

    public String deactivateUser(int userId) {
        User user = userDAO.findById(userId);

        if (user == null) {
            return "User not found.";
        }

        int result = userDAO.deactivate(userId);

        if (result > 0) {
            return "User deactivated successfully.";
        }

        return "Failed to deactivate user.";
    }

    private void prepareUserBeforeSave(User user) {
        if (user.getStatus() == null || user.getStatus().trim().isEmpty()) {
            user.setStatus("ACTIVE");
        }

        if (user.getProfileImage() == null || user.getProfileImage().trim().isEmpty()) {
            user.setProfileImage(null);
        }

        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            user.setAddress(null);
        }
    }

    private void prepareUserBeforeUpdate(User user) {
        if (user.getStatus() == null || user.getStatus().trim().isEmpty()) {
            user.setStatus("ACTIVE");
        }

        if (user.getProfileImage() == null || user.getProfileImage().trim().isEmpty()) {
            user.setProfileImage(null);
        }

        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            user.setAddress(null);
        }
    }

    public String validateUser(User user) {
        if (user == null) {
            return "User details are missing.";
        }

        if (isBlank(user.getFullName())) {
            return "Full name is required.";
        }

        if (isBlank(user.getEmail())) {
            return "Email is required.";
        }

        if (!user.getEmail().contains("@")) {
            return "Please enter a valid email address.";
        }

        if (isBlank(user.getPassword())) {
            return "Password is required.";
        }

        if (user.getPassword().length() < 8) {
            return "Password must have at least 8 characters.";
        }

        if (isBlank(user.getPhoneNumber())) {
            return "Phone number is required.";
        }

        if (isBlank(user.getStatus())) {
            return "Status is required.";
        }

        if (!user.getStatus().equals("ACTIVE") && !user.getStatus().equals("INACTIVE")) {
            return "Status must be ACTIVE or INACTIVE.";
        }

        return null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}
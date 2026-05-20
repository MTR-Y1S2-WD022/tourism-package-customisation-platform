package com.tourismplatform.service;

import com.tourismplatform.dao.UserDAO;
import com.tourismplatform.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements AccountOperations<User> {

    private final UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    // 1. RUNTIME POLYMORPHISM (OVERRIDING INTERFACE METHODS)

    @Override
    public User login(String email, String password) {
        if (email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            return null;
        }
        return userDAO.findByEmailAndPassword(email.trim(), password.trim());
    }

    @Override
    public boolean update(User user) {
        prepareUserBeforeUpdate(user);
        return userDAO.update(user) > 0;
    }

    @Override
    public void logout(HttpSession session) {
        session.removeAttribute("loggedInUser");
        session.removeAttribute("loggedInUserId");
        session.invalidate();
    }

    @Override
    public String validate(User user) {
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) return "Full name is required.";
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) return "Email is required.";
        if (!user.getEmail().contains("@")) return "Please enter a valid email address.";
        if (user.getPassword() == null || user.getPassword().trim().isEmpty()) return "Password is required.";
        if (user.getPassword().length() < 8) return "Password must have at least 8 characters.";
        if (user.getPhoneNumber() == null || user.getPhoneNumber().trim().isEmpty()) return "Phone number is required.";
        return null;
    }

    // OTHER METHODS

    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

    public User getUserById(int userId) {
        return userDAO.findById(userId);
    }

    public boolean emailExists(String email) {
        return userDAO.emailExists(email);
    }

    public boolean registerUser(User user) {
        prepareUserBeforeSave(user);
        return userDAO.save(user) > 0;
    }

    public void deleteUser(int userId) {
        userDAO.deleteUser(userId);
    }

    private void prepareUserBeforeSave(User user) {
        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            user.setAddress(null);
        }
    }

    private void prepareUserBeforeUpdate(User user) {
        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            user.setAddress(null);
        }
    }
}
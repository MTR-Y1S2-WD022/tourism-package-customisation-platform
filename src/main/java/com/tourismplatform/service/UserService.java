package com.tourismplatform.service;

import com.tourismplatform.dao.UserDAO;
import com.tourismplatform.model.User;
import org.springframework.beans.factory.annotation.Autowired;
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
        return userDAO.update(user) > 0;
    }

    public void deleteUser(int userId) {
        userDAO.deleteUser(userId);
    }


    private void prepareUserBeforeSave(User user) {

        if (user.getProfileImage() == null || user.getProfileImage().trim().isEmpty()) {
            user.setProfileImage(null);
        }

        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            user.setAddress(null);
        }
    }

    private void prepareUserBeforeUpdate(User user) {

        if (user.getProfileImage() == null || user.getProfileImage().trim().isEmpty()) {
            user.setProfileImage(null);
        }

        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            user.setAddress(null);
        }
    }

    public String validateUser(User user) {
        return user.validate();
    }

}
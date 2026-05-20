package com.tourismplatform.service;

import jakarta.servlet.http.HttpSession;

public interface AccountOperations<T> {

    T login(String email, String password);

    boolean update(T account);

    void logout(HttpSession session);

    String validate(T account);
}
package com.oluwaseyi.in.Moneymanager.interfaces;

import com.oluwaseyi.in.Moneymanager.entity.User;

import java.util.Optional;

public interface UserService {

    User save(User user);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);

    String createPasswordResetToken(User user);

    void updatePassword(User user, String newPassword);
}

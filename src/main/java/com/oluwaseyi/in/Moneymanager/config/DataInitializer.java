package com.oluwaseyi.in.Moneymanager.config;

import com.oluwaseyi.in.Moneymanager.dto.Role;
import com.oluwaseyi.in.Moneymanager.entity.User;
import com.oluwaseyi.in.Moneymanager.repository.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
public class DataInitializer {

    @Bean
    public ApplicationRunner initializeUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@example.com");
                admin.setPassword(passwordEncoder.encode("Admin123!"));
                admin.setRoles(Set.of(Role.ROLE_ADMIN));
                userRepository.save(admin);
            }

            if (userRepository.findByUsername("manager").isEmpty()) {
                User manager = new User();
                manager.setUsername("manager");
                manager.setEmail("manager@example.com");
                manager.setPassword(passwordEncoder.encode("Manager123!"));
                manager.setRoles(Set.of(Role.ROLE_MANAGER));
                userRepository.save(manager);
            }

            if (userRepository.findByUsername("user").isEmpty()) {
                User user = new User();
                user.setUsername("user");
                user.setEmail("user@example.com");
                user.setPassword(passwordEncoder.encode("User123!"));
                user.setRoles(Set.of(Role.ROLE_USER));
                userRepository.save(user);
            }
        };
    }
}

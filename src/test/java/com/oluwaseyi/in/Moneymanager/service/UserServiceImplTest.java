package com.oluwaseyi.in.Moneymanager.service;

import com.oluwaseyi.in.Moneymanager.entity.User;
import com.oluwaseyi.in.Moneymanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeEach
    void setUp() {
    }

    @Test
    void save_encodesPasswordAndPersistsUser() {
        User user = new User("jane", "jane@example.com", "PlainPassword", null);
        when(passwordEncoder.encode("PlainPassword")).thenReturn("encoded-password");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.save(user);

        verify(passwordEncoder).encode("PlainPassword");
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getPassword()).isEqualTo("encoded-password");
        assertThat(result).isSameAs(user);
    }

    @Test
    void createPasswordResetToken_assignsTokenAndExpiration() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        String token = userService.createPasswordResetToken(user);

        verify(userRepository).save(user);
        assertThat(token).isNotBlank();
        assertThat(user.getResetToken()).isEqualTo(token);
        assertThat(user.getResetTokenExpiration()).isNotNull();
    }

    @Test
    void updatePassword_encodesNewPasswordAndClearsResetFields() {
        User user = new User();
        when(passwordEncoder.encode("NewPassword123!")).thenReturn("encoded-new-password");

        userService.updatePassword(user, "NewPassword123!");

        verify(passwordEncoder).encode("NewPassword123!");
        verify(userRepository).save(userCaptor.capture());
        assertThat(userCaptor.getValue().getPassword()).isEqualTo("encoded-new-password");
        assertThat(userCaptor.getValue().getResetToken()).isNull();
        assertThat(userCaptor.getValue().getResetTokenExpiration()).isNull();
    }

    @Test
    void getAllUsers_returnsPagedResult() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(new User()));
        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<User> result = userService.getAllUsers(pageable);

        assertThat(result).isSameAs(page);
    }
}

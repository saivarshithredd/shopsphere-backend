package com.example.demo.service.impl;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setEmail("test@mail.com");
        user.setPassword("encodedPass");
        user.setRole("USER");
    }

    @Test
    void signup_success() {
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.empty());

        when(passwordEncoder.encode("1234")).thenReturn("encodedPass");

        String result = authService.signup("Sai", "test@mail.com", "1234", "USER");

        assertEquals("User registered", result);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void signup_userAlreadyExists() {
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        String result = authService.signup("Sai", "test@mail.com", "1234", "USER");

        assertEquals("User already exists", result);
        verify(userRepository, never()).save(any());
    }

    @Test
    void login_success() {
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("1234", "encodedPass")).thenReturn(true);

        String token = authService.login("test@mail.com", "1234");

        assertNotNull(token);
    }

    @Test
    void login_userNotFound() {
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> authService.login("test@mail.com", "1234"));
    }

    @Test
    void login_invalidPassword() {
        when(userRepository.findByEmail("test@mail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches("wrong", "encodedPass"))
                .thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> authService.login("test@mail.com", "wrong"));
    }
}
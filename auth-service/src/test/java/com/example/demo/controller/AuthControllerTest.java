package com.example.demo.controller;

import com.example.demo.dto.AuthRequest;
import com.example.demo.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) 
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void signup_success() throws Exception {

        AuthRequest req = new AuthRequest();
        req.setName("Sai");
        req.setEmail("test@mail.com");
        req.setPassword("1234");
        req.setRole("USER");

        Mockito.when(authService.signup(
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
                .thenReturn("User registered");

        mockMvc.perform(post("/signup")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().string("User registered"));
    }

    @Test
    void login_success() throws Exception {

        AuthRequest req = new AuthRequest();
        req.setEmail("test@mail.com");
        req.setPassword("1234");

        Mockito.when(authService.login(Mockito.any(), Mockito.any()))
                .thenReturn("jwt-token");

        mockMvc.perform(post("/login")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }
}
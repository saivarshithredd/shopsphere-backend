package com.example.demo.security;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    @Test
    void generate_and_validate_token() {

        String token = JwtUtil.generateToken("test@mail.com", "USER");

        assertNotNull(token);
        assertEquals("test@mail.com", JwtUtil.extractEmail(token));
        assertEquals("USER", JwtUtil.extractRole(token));
    }
}
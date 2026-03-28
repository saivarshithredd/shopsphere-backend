package com.example.demo.security;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.*;

import jakarta.servlet.FilterChain;

import static org.mockito.Mockito.*;

class JwtFilterTest {

    @Test
    void doFilter_withoutToken() throws Exception {

        JwtFilter filter = new JwtFilter();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/test");

        MockHttpServletResponse response = new MockHttpServletResponse();

        FilterChain chain = mock(FilterChain.class);

        filter.doFilter(request, response, chain);

        verify(chain, times(1)).doFilter(request, response);
    }
}
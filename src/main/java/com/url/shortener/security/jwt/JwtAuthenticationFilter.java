package com.url.shortener.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtTokenProvider;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // ==========================================
        // Skip JWT processing for Actuator Health
        // ==========================================
        if (request.getRequestURI().equals("/actuator/health")) {

            filterChain.doFilter(request, response);
            return;
        }

        try {

            // ==========================================
            // Get JWT from Authorization Header
            // ==========================================
            String jwt =
                    jwtTokenProvider.getJwtFromHeader(request);

            // ==========================================
            // Validate JWT
            // ==========================================
            if (jwt != null &&
                    jwtTokenProvider.validateToken(jwt)) {

                // ==========================================
                // Get Username from JWT
                // ==========================================
                String username =
                        jwtTokenProvider
                                .getUserNameFromJwtToken(jwt);

                // ==========================================
                // Load User Details
                // ==========================================
                UserDetails userDetails =
                        userDetailsService
                                .loadUserByUsername(username);

                // ==========================================
                // Set Authentication
                // ==========================================
                if (userDetails != null) {

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }

        } catch (Exception e) {

            // Invalid JWT should not stop the request
            System.out.println(
                    "JWT Authentication failed: "
                            + e.getMessage()
            );
        }

        // ==========================================
        // Continue Filter Chain
        // ==========================================
        filterChain.doFilter(request, response);
    }
}
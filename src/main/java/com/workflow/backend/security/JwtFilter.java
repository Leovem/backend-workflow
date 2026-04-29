package com.workflow.backend.security;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        
    
        String authHeader = request.getHeader("Authorization");
        String path = request.getServletPath();
        System.out.println("\n--- [DEBUG] Petición entrante a: " + path + " ---");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            String email = jwtService.extractEmail(token);

            
            if (email != null && org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication() == null) {
                
                
                org.springframework.security.authentication.UsernamePasswordAuthenticationToken authToken = 
                    new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                        email, null, java.util.Collections.emptyList()
                    );

                
                org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(authToken);
                
                System.out.println("✅ Seguridad establecida para: " + email);
            }

        } catch (Exception e) {
            System.err.println("❌ Token inválido: " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
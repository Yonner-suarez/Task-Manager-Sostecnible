package com.sostecnible.TaskManager.infraestructure.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.sostecnible.TaskManager.domain.ports.out.TokenService;

import java.io.IOException;
import java.util.Collections;

@Component // <-- Vital
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService; // Tu clase que valida el JWT

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        
        String authHeader = request.getHeader("Authorization");
        System.out.println("DEBUG: Auth Header -> " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            
            try {
                if (tokenService.validateToken(token)) {
                    Long userId = tokenService.extractUserId(token);
                    
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            userId, null, Collections.emptyList());
                    
                    SecurityContextHolder.getContext().setAuthentication(auth);
                    System.out.println("DEBUG: Usuario autenticado correctamente: " + userId);
                }
            } catch (Exception e) {
                System.out.println("DEBUG: Error validando token: " + e.getMessage());
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}
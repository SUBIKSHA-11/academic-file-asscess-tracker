package com.university.securetracker.security;


import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.university.securetracker.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
//@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository repo;

    public JwtFilter(JwtUtil jwtUtil, UserRepository repo) {
        this.jwtUtil = jwtUtil;
        this.repo = repo;
    }

    @Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain filterChain)
        throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {

        String token = authHeader.substring(7);

        try {
            String email = jwtUtil.extractEmail(token);

            if (email != null) {

                var user = repo.findByEmail(email).orElse(null);

                if (user != null) {

                    var authorities = Collections.singletonList(
                            new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase())
                    );

                   UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(
                email,
                null,
                authorities
        );

authToken.setDetails(
        new org.springframework.security.web.authentication.WebAuthenticationDetailsSource()
                .buildDetails(request)
);
System.out.println("JWT FILTER HIT");
System.out.println("Email from token: " + email);
System.out.println("Role from DB: " + user.getRole());


SecurityContextHolder.getContext().setAuthentication(authToken);

                }
            }

        } catch (Exception e) {
            // token invalid → ignore
        }
    }

    filterChain.doFilter(request, response);
}

}

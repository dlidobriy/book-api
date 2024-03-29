package com.example.bookapi.security;

import com.example.bookapi.service.AuthServise;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    final JwtProvider jwtProvider;
    final AuthServise authServise;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getRequestURI().startsWith("/api/auth/") || !request.getRequestURI().startsWith("/api/apidoc/")) {
            String token = request.getHeader("Authorization");
            if (token != null) {
                token = token.substring(7);
                if (jwtProvider.validateToken(token)) {
                    if (jwtProvider.expireToken(token)) {
                        String usernameFromToken = jwtProvider.getUsernameFromToken(token);
                        UserDetails userDetails = authServise.loadUserByUsername(usernameFromToken);
                        System.out.println(userDetails);
                        UsernamePasswordAuthenticationToken authenticationToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                        System.out.println(authenticationToken);
                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                        System.out.println(SecurityContextHolder.getContext().getAuthentication());
                    }
                }
            }

        }

        doFilter(request, response, filterChain);


    }
}
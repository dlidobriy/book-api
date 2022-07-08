package com.example.bookapi.service;

import com.example.bookapi.entity.User;
import com.example.bookapi.repositoriy.UserRepositary;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServise implements UserDetailsService {

    final UserRepositary userRepositary;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       Optional<User> userOptional=userRepositary.findByUserName(username);
       return userOptional.orElseThrow(() ->new UsernameNotFoundException("User Topilmadi"));
    }
}

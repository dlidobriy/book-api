package com.example.bookapi.controller;

import com.example.bookapi.dto.ApiResponse;
import com.example.bookapi.dto.LoginDto;
import com.example.bookapi.entity.User;
import com.example.bookapi.entity.enums.Role;
import com.example.bookapi.repositoriy.UserRepositary;
import com.example.bookapi.security.JwtProvider;
import com.example.bookapi.service.AuthServise;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    final JwtProvider jwtProvider;
    final AuthServise authServise;
    final UserRepositary userRepositary;
    final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        UserDetails userDetails = authServise.loadUserByUsername(loginDto.getUserName());
        String token = jwtProvider.generateToken(loginDto.getUserName());
        return ResponseEntity.ok().body(token);
    }

    @PostMapping("/register")
    public ResponseEntity loginUser( @RequestBody LoginDto loginDto) {
        Optional<User> userOptional = userRepositary.findByUserName(loginDto.getUserName());
        if (userOptional.isPresent()){
            return ResponseEntity.status(400).body("This username already exist!");
        }
        User user = new User();
        user.setUserName(loginDto.getUserName());
        user.setPassword(passwordEncoder.encode(loginDto.getPassword()));
        user.setRole(Role.USER);
        userRepositary.save(user);
        String token = jwtProvider.generateToken(loginDto.getUserName());
        return ResponseEntity.ok().body(token);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}

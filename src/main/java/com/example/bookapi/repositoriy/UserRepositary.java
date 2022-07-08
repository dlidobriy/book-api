package com.example.bookapi.repositoriy;

import com.example.bookapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositary extends JpaRepository<User,Integer> {


    Optional<User>findByUserName(String userName);

}

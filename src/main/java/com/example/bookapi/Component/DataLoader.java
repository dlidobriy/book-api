package com.example.bookapi.Component;

import com.example.bookapi.entity.Group;
import com.example.bookapi.entity.User;
import com.example.bookapi.entity.enums.Role;
import com.example.bookapi.repositoriy.GroupRepositary;
import com.example.bookapi.repositoriy.UserRepositary;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    @Value("${spring.sql.init.mode}")
    String intmode;

    final UserRepositary userRepositary;
    private final PasswordEncoder passwordEncoder;
    final GroupRepositary groupRepositary;


    @Override
    public void run(String... args) throws Exception {


        if (intmode.equals("always")) {
            userRepositary.save(new User("dlidobriy", passwordEncoder.encode("dlidobriy"), Role.ADMIN));

            for (int i = 1; i <= 11; i++) {
                groupRepositary.save(new Group(i));
            }


        }
    }
}

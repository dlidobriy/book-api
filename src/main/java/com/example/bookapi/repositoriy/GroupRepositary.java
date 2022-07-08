package com.example.bookapi.repositoriy;

import com.example.bookapi.entity.Group;
import com.example.bookapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepositary extends JpaRepository<Group,Integer> {

}

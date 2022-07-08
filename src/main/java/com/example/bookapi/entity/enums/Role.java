package com.example.bookapi.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Collection;
import java.util.List;
@Getter
@AllArgsConstructor
public enum Role {
        USER(List.of("READ")),
        ADMIN(List.of("CRUD", "READ"));
        private final List<String> authorities;
}

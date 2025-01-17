package com.ohgiraffers.ukki.common;

public enum UserRole {
    USER("USER"),
    BAD_USER("BAD_USER"),
    STORE("STORE"),
    ADMIN("ADMIN");

    private String role;

    UserRole() {}

    UserRole(String role) {
        this.role=role;
    }

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "role='" + role + '\'' +
                '}';
    }
}

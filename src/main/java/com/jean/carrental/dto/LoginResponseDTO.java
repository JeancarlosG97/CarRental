package com.jean.carrental.dto;

public class LoginResponseDTO {
    private Long id;
    private String email;
    private String token;
    private String role;

    public LoginResponseDTO(Long id, String email, String token, String role) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return email;
    }

    public void setUsername(String username) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
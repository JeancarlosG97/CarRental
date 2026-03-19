package com.jean.carrental.dto;

public class LoginResponseDTO {
    private Long id;
    private String username;
    private String token;
    private String role;

    public LoginResponseDTO(Long id, String username, String token, String role) {
        this.id = id;
        this.username = username;
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
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
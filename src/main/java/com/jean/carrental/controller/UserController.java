package com.jean.carrental.controller;

import com.jean.carrental.dto.LoginRequestDTO;
import com.jean.carrental.dto.LoginResponseDTO;
import com.jean.carrental.dto.UserDTO;
import com.jean.carrental.security.JwtUtil;
import com.jean.carrental.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO loginRequest) {
        UserDTO user = userService.login(loginRequest.getUsername(), loginRequest.getPassword());
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return new LoginResponseDTO(
                user.getId(),
                user.getUsername(),
                token,
                user.getRole()
        );
    }
}

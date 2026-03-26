package com.jean.carrental;

import com.jean.carrental.security.JwtUtil;
import org.springframework.boot.CommandLineRunner;

public class JwtTestRunner implements CommandLineRunner {

    private final JwtUtil jwtUtil;

    public JwtTestRunner(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void run(String... args) throws Exception {
        String adminToken = jwtUtil.generateToken("adminUser", "ADMIN");
        String userToken = jwtUtil.generateToken("userUser", "USER");

        System.out.println("Admin Token: " + adminToken);
        System.out.println("User Token: " + userToken);
    }
}

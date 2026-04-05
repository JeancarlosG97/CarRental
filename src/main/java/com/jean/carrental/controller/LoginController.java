package com.jean.carrental.controller;

import com.jean.carrental.dto.LoginRequestDTO;
import com.jean.carrental.dto.LoginResponseDTO;
import com.jean.carrental.dto.RegisterRequestDTO;
import com.jean.carrental.model.Customer;
import com.jean.carrental.model.Role;
import com.jean.carrental.repository.CustomerRepository;
import com.jean.carrental.security.JwtUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Slf4j
public class LoginController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginController(CustomerRepository customerRepository,
                           PasswordEncoder passwordEncoder,
                           JwtUtil jwtUtil) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Registration
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequestDTO request) {
        log.info("Register request: {}", request);

        if (customerRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        Customer customer = new Customer();
        customer.setName(request.getName());
        customer.setEmail(request.getEmail());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));

        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setRole(Role.USER);

        customerRepository.save(customer);

        return ResponseEntity.ok("User registered successfully");
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
        log.info("Login request: {}", loginRequest);

        Optional<Customer> customerOpt = customerRepository.findByEmail(loginRequest.getEmail());

        if (customerOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        Customer customer = customerOpt.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), customer.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(customer.getEmail(), customer.getRole().name());

        LoginResponseDTO response = new LoginResponseDTO(
                (long) customer.getId(),
                customer.getEmail(),
                token,
                customer.getRole().name()
        );

        return ResponseEntity.ok(response);
    }
}

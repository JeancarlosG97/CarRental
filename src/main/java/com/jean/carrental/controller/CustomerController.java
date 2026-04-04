package com.jean.carrental.controller;

import com.jean.carrental.service.CustomerService;
import com.jean.carrental.dto.CustomerDTO;
import com.jean.carrental.model.Customer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // User AND Admin

    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CustomerDTO getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        log.info("User {} requested their own profile", email);
        return customerService.getCustomerByEmail(email);
    }

    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CustomerDTO updateMyProfile(Authentication authentication,
                                       @Valid @RequestBody Customer customer) {
        String email = authentication.getName();
        log.info("User {} updating their own profile", email);
        return customerService.updateCustomerByEmail(email, customer);
    }

    // Only Admin

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CustomerDTO> getAllCustomers() {
        log.info("ADMIN requested all customers");
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CustomerDTO getCustomerById(@PathVariable int id) {
        log.info("ADMIN requested customer with ID {}", id);
        return customerService.getCustomerById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable int id) {
        log.info("ADMIN deleting customer with ID {}", id);
        customerService.deleteCustomer(id);
    }
}
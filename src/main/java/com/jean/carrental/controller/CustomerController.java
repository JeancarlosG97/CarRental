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


/**
 * REST controller for managing customer-related operations.
 * Provides endpoints for user profile management and administrative customer access.
 * Access is controlled based on roles (USER, ADMIN).
 */
@RestController
@RequestMapping("/customers")
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * Retrieves the profile of the currently authenticated user.
     * Accessible by both USER and ADMIN roles.
     *
     * @param authentication the authentication object containing the user's identity
     * @return the current user's profile as a CustomerDTO
     */
    @GetMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CustomerDTO getMyProfile(Authentication authentication) {
        String email = authentication.getName();
        log.info("User {} requested their own profile", email);
        return customerService.getCustomerByEmail(email);
    }

    /**
     * Updates the profile of the currently authenticated user.
     * Only allows modification of permitted fields (e.g., name, phone, password).
     * Accessible by both USER and ADMIN roles.
     *
     * @param authentication the authentication object containing the user's identity
     * @param customer the updated customer data
     * @return the updated customer profile as a CustomerDTO
     */
    @PutMapping("/me")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CustomerDTO updateMyProfile(Authentication authentication,
                                       @Valid @RequestBody Customer customer) {
        String email = authentication.getName();
        log.info("User {} updating their own profile", email);
        return customerService.updateCustomerByEmail(email, customer);
    }

    /**
     * Retrieves all customers in the system.
     * Only accessible by ADMIN users.
     *
     * @return list of all customers as CustomerDTO objects
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<CustomerDTO> getAllCustomers() {
        log.info("ADMIN requested all customers");
        return customerService.getAllCustomers();
    }

    /**
     * Retrieves a specific customer by their ID.
     * Only accessible by ADMIN users.
     *
     * @param id the ID of the customer
     * @return the requested customer as a CustomerDTO
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CustomerDTO getCustomerById(@PathVariable int id) {
        log.info("ADMIN requested customer with ID {}", id);
        return customerService.getCustomerById(id);
    }

    /**
     * Deletes a customer by their ID.
     * Only accessible by ADMIN users.
     *
     * @param id the ID of the customer to delete
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable int id) {
        log.info("ADMIN deleting customer with ID {}", id);
        customerService.deleteCustomer(id);
    }
}
package com.jean.carrental.service;

import com.jean.carrental.dto.CustomerDTO;
import com.jean.carrental.exception.CustomerNotFoundException;
import com.jean.carrental.model.Role;
import com.jean.carrental.repository.CustomerRepository;
import com.jean.carrental.model.Customer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomerDTO> getAllCustomers() {
        log.info("Fetching all customers");

        return customerRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(int id) {
        log.info("Fetching customer with id: {}", id);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer with id {} not found", id);
                    return new CustomerNotFoundException(id);
                });
        return convertToDTO(customer);
    }

    public CustomerDTO addCustomer(@Valid @RequestBody Customer customer) {
        log.info("Adding new customer: {}", customer.getEmail());
        customer.setRole(Role.USER);
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer added with id: {}", savedCustomer.getId());

        return convertToDTO(savedCustomer);
    }

    public CustomerDTO updateCustomer(int id, Customer updatedCustomer) {
        log.info("Updating customer with id: {}", id);
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer with id {} not found for update", id);
                    return new CustomerNotFoundException(id);
                });

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());

        Customer savedCustomer = customerRepository.save(existingCustomer);
        log.info("Customer with id {} updated successfully", savedCustomer.getId());
        return convertToDTO(savedCustomer);
    }

    public void deleteCustomer(int id) {

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer with id {} not found for deletion", id);
                    return new CustomerNotFoundException(id);
                });

        customerRepository.delete(customer);
        log.info("Customer with id {} deleted successfully", id);
    }

    private CustomerDTO convertToDTO(Customer customer) {
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhoneNumber(),
                customer.getRole().name()
        );
    }

    public CustomerDTO getCustomerByEmail(String email) {
        log.info("Fetching customer with email: {}", email);

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with this email: " + email));

        return convertToDTO(customer);
    }

    public CustomerDTO updateCustomerByEmail(String email, Customer updatedCustomer) {
        log.info("Updating customer with email: {}", email);

        Customer existingCustomer = customerRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Customer with email {} not found", email);
                    return new CustomerNotFoundException("Customer not found with email: " + email);
                });

        // Update only the allowed fields
        if(updatedCustomer.getName() != null) {
            existingCustomer.setName(updatedCustomer.getName());
        }
        if (updatedCustomer.getPhoneNumber() != null) {
            existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
        }
        if(updatedCustomer.getPassword() != null && !updatedCustomer.getPassword().isBlank()) {
            existingCustomer.setPassword(passwordEncoder.encode(updatedCustomer.getPassword()));
        }

        Customer savedCustomer = customerRepository.save(existingCustomer);
        log.info("Updated customer with email: {}", email);

        return convertToDTO(savedCustomer);
    }
}
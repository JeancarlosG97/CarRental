package com.jean.carrental.service;

import com.jean.carrental.dto.CustomerDTO;
import com.jean.carrental.exception.CustomerNotFoundException;
import com.jean.carrental.model.Role;
import com.jean.carrental.repository.CustomerRepository;
import com.jean.carrental.model.Customer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CustomerService {

    public final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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

    public void deleteCustomer(int id, Customer requester) {
        log.info("User {} requested deletion of customer with id: {}", requester.getId(), id);

        if (requester.getRole() != Role.ADMIN) {
            log.warn("Access denied for user {}: Admins only" , requester.getId());
            throw new RuntimeException("Access denied: Admins only");
        }

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Customer with id {} not found for deletion", id);
                    return new CustomerNotFoundException(id);
                });

        customerRepository.delete(customer);
        log.info("Customer with id {} deleted successfully by admin {}", id, requester.getId());
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
}
package com.jean.carrental.service;

import com.jean.carrental.dto.CustomerDTO;
import com.jean.carrental.exception.CustomerNotFoundException;
import com.jean.carrental.model.Role;
import com.jean.carrental.repository.CustomerRepository;
import com.jean.carrental.model.Customer;
import jakarta.validation.Valid;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.slf4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    public final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CustomerDTO getCustomerById(int id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        return convertToDTO(customer);
    }

    public CustomerDTO addCustomer(@Valid @RequestBody Customer customer) {
        logger.info("Attempting to add a new customer with email: {}", customer.getEmail());

        customer.setRole(Role.USER);
        logger.debug("Default role set to USER for customer: {}", customer.getEmail());

        Customer savedCustomer = customerRepository.save(customer);
        logger.info("Customer successfully saved with ID: {} and role: {}", savedCustomer.getId(), savedCustomer.getRole());

        CustomerDTO customerDTO = convertToDTO(savedCustomer);
        logger.debug("CustomerDTO created: {}", customerDTO);
        
        return customerDTO;
    }

    public CustomerDTO updateCustomer(int id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());

        Customer savedCustomer = customerRepository.save(existingCustomer);
        return convertToDTO(savedCustomer);
    }

    public void deleteCustomer(int id, Customer requester) {
        if (requester.getRole() != Role.ADMIN) {
            throw new RuntimeException("Access denied: Admins only");
        }

        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        customerRepository.delete(customer);
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
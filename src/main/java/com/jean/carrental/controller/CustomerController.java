package com.jean.carrental.controller;

import com.jean.carrental.service.CustomerService;
import com.jean.carrental.dto.CustomerDTO;
import com.jean.carrental.model.Customer;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

    @GetMapping
    public List<CustomerDTO> getAllCustomers() {
        log.info("Receive request to fetch all customers");
        return customerService.getAllCustomers();
    }

    @GetMapping("/{id}")
    public CustomerDTO getCustomerById(@PathVariable int id) {
        log.info("Received request to fetch customer with id: {}", id);
        return customerService.getCustomerById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO addCustomer(@Valid @RequestBody Customer customer) {
        log.info("Received request to add customer with email: {}", customer.getEmail());
        return customerService.addCustomer(customer);
    }

    @PutMapping("/{id}")
    public CustomerDTO updateCustomer(@PathVariable int id, @Valid @RequestBody Customer customer) {
        log.info("Received request to update customer with id: {}", id);
        return customerService.updateCustomer(id, customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable int id, @RequestBody Customer requester) {
        log.info("Received request to delete customer {} by user {}", id, requester.getId());
        customerService.deleteCustomer(id, requester);
    }
}

package com.jean.carrental.Service;

import com.jean.carrental.Exception.CustomerNotFoundException;
import com.jean.carrental.Repository.CustomerRepository;
import com.jean.carrental.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    public final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(int id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }

    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(int id, Customer updatedCustomer) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        existingCustomer.setName(updatedCustomer.getName());
        existingCustomer.setEmail(updatedCustomer.getEmail());
        existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());

        return customerRepository.save(existingCustomer);
    }

    public void deleteCustomer(int id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        customerRepository.delete(customer);
    }
}
package com.jean.carrental.Exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(int id) {
        super("Customer not found with id: " + id);
    }
}

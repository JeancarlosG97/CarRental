package com.jean.carrental.exception;

public class CustomerNotFoundException extends RuntimeException {
    public CustomerNotFoundException(int id) {
        super("Customer not found with id: " + id);
    }

    public CustomerNotFoundException(String message) {
        super(message);
    }
}

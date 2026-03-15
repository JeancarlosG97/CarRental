package com.jean.carrental.exception;

public class CarNotFoundException extends RuntimeException {
    public CarNotFoundException(int id) {
        super("Car not found with id: " + id);
    }
}

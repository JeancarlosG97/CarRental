package com.jean.carrental.Exception;

public class RentalNotFoundException extends RuntimeException{
    public RentalNotFoundException(int id) {
        super("Rental not found with id: " + id);
    }
}

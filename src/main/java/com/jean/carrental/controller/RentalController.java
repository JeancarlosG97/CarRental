package com.jean.carrental.controller;

import com.jean.carrental.service.RentalService;
import com.jean.carrental.dto.RentalDTO;
import com.jean.carrental.model.Rental;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
@Slf4j
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RentalDTO> getAllRentals() {
        log.info("Received request to fetch all rentals");
        return rentalService.getAllRentals();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RentalDTO getRentalById(@PathVariable int id) {
        log.info("Received request to fetch rental with id: {}", id);
        return rentalService.getRentalById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public RentalDTO addRental(@Valid @RequestBody Rental rental) {
        log.info("Received request to create a new rental");
        return rentalService.addRental(rental);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RentalDTO updateRental(@PathVariable int id, @Valid @RequestBody Rental rental) {
        log.info("Received request to update rental with id: {}", id);
        return rentalService.updateRental(id, rental);
    }

    @PutMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public RentalDTO returnRental(@PathVariable int id) {
        log.info("Received request to return rental with id: {}", id);
        return rentalService.returnRental(id);
    }

    @PostMapping("/{customerId}/{carId}/{rentalDays}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public RentalDTO rentCar(@PathVariable int customerId, @PathVariable int carId, @PathVariable int rentalDays){
        log.info("Received request to rent car {} for customer {} for {} days", carId, customerId, rentalDays);
        return rentalService.rentCar(customerId, carId, rentalDays);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRental(@PathVariable int id) {
        log.info("Received request to delete rental id: {}", id);
        rentalService.deleteRental(id);
    }
}

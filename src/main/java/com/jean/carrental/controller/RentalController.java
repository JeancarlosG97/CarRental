package com.jean.carrental.controller;

import com.jean.carrental.service.RentalService;
import com.jean.carrental.dto.RentalDTO;
import com.jean.carrental.model.Rental;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
        log.info("ADMIN requested all rentals");
        return rentalService.getAllRentals();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RentalDTO getRentalById(@PathVariable int id) {
        log.info("ADMIN requested rental with id: {}", id);
        return rentalService.getRentalById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public RentalDTO addRental(@Valid @RequestBody Rental rental) {
        log.info("ADMIN creating rental for customer {} and car {}", rental.getCustomer().getId(), rental.getCar().getId());
        return rentalService.addRental(rental);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RentalDTO updateRental(@PathVariable int id, @Valid @RequestBody Rental rental) {
        log.info("ADMIN updating rental with id: {}", id);
        return rentalService.updateRental(id, rental);
    }

    @PutMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public RentalDTO returnRental(@PathVariable int id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} returning rental with id {}", email, id);
        return rentalService.returnRental(id);
    }

    @PostMapping("/{carId}/{rentalDays}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public RentalDTO rentCar(@PathVariable int carId, @PathVariable int rentalDays) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} renting  car {} for {} days", email, carId, rentalDays);
        return rentalService.rentCar(carId, rentalDays);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRental(@PathVariable int id) {
        log.info("ADMIN deleting rental ID {}", id);
        rentalService.deleteRental(id);
        log.info("Rental deleted: ID {}", id);
    }

    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<RentalDTO> getMyRentals() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} requested their rentals", email);
        return rentalService.getMyRentals();
    }

}

package com.jean.carrental.controller;

import com.jean.carrental.service.RentalService;
import com.jean.carrental.dto.RentalDTO;
import com.jean.carrental.model.Rental;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rentals")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public List<RentalDTO> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/{id}")
    public RentalDTO getRentalById(@PathVariable int id) {
        return rentalService.getRentalById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RentalDTO addRental(@Valid @RequestBody Rental rental) {
        return rentalService.addRental(rental);
    }

    @PutMapping("/{id}")
    public RentalDTO updateRental(@PathVariable int id, @Valid @RequestBody Rental rental) {
        return rentalService.updateRental(id, rental);
    }

    @PutMapping("/{id}/return")
    public RentalDTO returnRental(@PathVariable int id) {
        return rentalService.returnRental(id);
    }

    @PostMapping("/{customerId}/{carId}/{rentalDays}")
    @ResponseStatus(HttpStatus.CREATED)
    public RentalDTO rentCar(@PathVariable int customerId, @PathVariable int carId, @PathVariable int rentalDays){
        return rentalService.rentCar(customerId, carId, rentalDays);
    }

    @DeleteMapping("/{id}")
    public void deleteRental(@PathVariable int id) {
        rentalService.deleteRental(id);
    }
}

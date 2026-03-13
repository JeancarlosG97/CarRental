package com.jean.carrental.Controller;

import com.jean.carrental.Service.RentalService;
import com.jean.carrental.model.Rental;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Rental")
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @GetMapping
    public List<Rental> getAllRentals() {
        return rentalService.getAllRentals();
    }

    @GetMapping("/{id}")
    public Rental getRentalById(@PathVariable int id) {
        return rentalService.getRentalById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Rental addRental(@Valid @RequestBody Rental rental) {
        return rentalService.addRental(rental);
    }

    @PutMapping("/{id}")
    public Rental updateRental(@PathVariable int id, @Valid @RequestBody Rental rental) {
        return rentalService.updateRental(id, rental);
    }

    @DeleteMapping("/{id}")
    public void deleteRental(@PathVariable int id) {
        rentalService.deleteRental(id);
    }
}

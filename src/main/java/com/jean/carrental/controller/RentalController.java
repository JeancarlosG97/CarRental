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

/**
 * REST controller for managing rental operations.
 * Handles rental creation, returns, and administrative management.
 * Enforces role-based access control and ownership rules.
 */
@RestController
@RequestMapping("/rentals")
@Slf4j
public class RentalController {

    private final RentalService rentalService;

    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    /**
     * Retrieves all rentals in the system.
     * Only accessible by ADMIN users.
     *
     * @return list of all rentals as RentalDTO objects
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<RentalDTO> getAllRentals() {
        log.info("ADMIN requested all rentals");
        return rentalService.getAllRentals();
    }

    /**
     * Retrieves a specific rental by its ID.
     * Only accessible by ADMIN users.
     *
     * @param id the ID of the rental
     * @return the requested rental as a RentalDTO
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RentalDTO getRentalById(@PathVariable int id) {
        log.info("ADMIN requested rental with id: {}", id);
        return rentalService.getRentalById(id);
    }

    /**
     * Creates a rental manually using full rental object.
     * Only accessible by ADMIN users.
     *
     * @param rental the rental object to create
     * @return the created rental as a RentalDTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public RentalDTO addRental(@Valid @RequestBody Rental rental) {
        log.info("ADMIN creating rental for customer {} and car {}", rental.getCustomer().getId(), rental.getCar().getId());
        return rentalService.addRental(rental);
    }

    /**
     * Updates an existing rental.
     * Only accessible by ADMIN users.
     *
     * @param id the ID of the rental to update
     * @param rental the updated rental data
     * @return the updated rental as a RentalDTO
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public RentalDTO updateRental(@PathVariable int id, @Valid @RequestBody Rental rental) {
        log.info("ADMIN updating rental with id: {}", id);
        return rentalService.updateRental(id, rental);
    }

    /**
     * Returns a rented car.
     * Accessible by the owner of the rental or an ADMIN.
     * Updates car availability and rental status.
     *
     * @param id the ID of the rental to return
     * @return the updated rental as a RentalDTO
     */
    @PutMapping("/{id}/return")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public RentalDTO returnRental(@PathVariable int id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} returning rental with id {}", email, id);
        return rentalService.returnRental(id);
    }

    /**
     * Allows a user to rent a car for a specified number of days.
     * The authenticated user is automatically associated with the rental.
     * Ensures the car is available before creating the rental.
     *
     * @param carId the ID of the car to rent
     * @param rentalDays the number of days to rent the car
     * @return the created rental as a RentalDTO
     */
    @PostMapping("/{carId}/{rentalDays}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public RentalDTO rentCar(@PathVariable int carId, @PathVariable int rentalDays) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} renting  car {} for {} days", email, carId, rentalDays);
        return rentalService.rentCar(carId, rentalDays);
    }

    /**
     * Allows an ADMIN to create a rental on behalf of any customer.
     * Bypasses the authenticated user and assigns the rental to the specified customer.
     *
     * @param customerId the ID of the customer
     * @param carId the ID of the car
     * @param rentalDays the number of days to rent the car
     * @return the created rental as a RentalDTO
     */
    @PostMapping("/admin/{customerId}/{carId}/{rentalDays}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public RentalDTO adminRentCar(@PathVariable int customerId, @PathVariable int carId, @PathVariable int rentalDays){
        log.info("ADMIN is manually creating a rental for customer {} | car {} | {} days", customerId, carId, rentalDays);
        return rentalService.rentCarForCustomer(customerId, carId, rentalDays);
    }

    /**
     * Deletes a rental by its ID.
     * Only accessible by ADMIN users.
     *
     * @param id the ID of the rental to delete
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteRental(@PathVariable int id) {
        log.info("ADMIN deleting rental ID {}", id);
        rentalService.deleteRental(id);
        log.info("Rental deleted: ID {}", id);
    }

    /**
     * Retrieves all rentals associated with the currently authenticated user.
     * Accessible by both USER and ADMIN roles.
     *
     * @return list of rentals belonging to the current user
     */
    @GetMapping("/my")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<RentalDTO> getMyRentals() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} requested their rentals", email);
        return rentalService.getMyRentals();
    }
}
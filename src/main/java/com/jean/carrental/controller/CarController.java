package com.jean.carrental.controller;

import com.jean.carrental.service.CarService;
import com.jean.carrental.dto.CarDTO;
import com.jean.carrental.model.Car;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * REST controller for managing car-related operations.
 * Provides endpoints for retrieving, creating, updating, and deleting cars.
 * Access is restricted based on user roles (USER, ADMIN)
 */

@RestController
@RequestMapping("/cars")
@Slf4j
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    /**
     * Retrieves all cars in the system.
     * Accessible by both USER and ADMIN roles.
     *
     * @return list of all cars as CarDTO objects
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<CarDTO> getAllCars() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} requested all cars", email);
        return carService.getAllCars();
    }

    /**
     * Retrieves all cars in the system.
     * Accessible by both USER and ADMIN roles.
     *
     * @return list of all cars as CarDTO objects
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CarDTO getCarById(@PathVariable int id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} requested car with ID {}", email, id);
        return carService.getCarById(id);
    }

    /**
     * Creates a new car in the system.
     * Only accessible by ADMIN users.
     *
     * @param car the car object to be created
     * @return the created car as a CarDTO
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public CarDTO addCar(@Valid @RequestBody Car car) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("ADMIN {} adding a new car: {} {}", email, car.getMake(), car.getModel());
        return carService.addCar(car);
    }

    /**
     * Updates an existing car by its ID.
     * Only accessible by ADMIN users.
     *
     * @param id the ID of the car to update
     * @param car the updated car data
     * @return the updated car as a CarDTO
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CarDTO updateCar(@PathVariable int id, @Valid @RequestBody Car car) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("ADMIN {} updating car ID {}", email, id);
        return carService.updateCar(id, car);
    }

    /**
     * Deletes a car by its ID.
     * Only accessible by ADMIN users.
     *
     * @param id the ID of the car to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCar(@PathVariable int id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("ADMIN {} deleting car ID {}", email, id);
        carService.deleteCar(id);
    }
}
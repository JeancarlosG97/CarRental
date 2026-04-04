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

@RestController
@RequestMapping("/cars")
@Slf4j
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public List<CarDTO> getAllCars() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} requested all cars", email);
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public CarDTO getCarById(@PathVariable int id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("User {} requested car with ID {}", email, id);
        return carService.getCarById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public CarDTO addCar(@Valid @RequestBody Car car) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("ADMIN {} adding a new car: {} {}", email, car.getMake(), car.getModel());
        return carService.addCar(car);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public CarDTO updateCar(@PathVariable int id, @Valid @RequestBody Car car) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("ADMIN {} updating car ID {}", email, id);
        return carService.updateCar(id, car);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCar(@PathVariable int id) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("ADMIN {} deleting car ID {}", email, id);
        carService.deleteCar(id);
    }
}
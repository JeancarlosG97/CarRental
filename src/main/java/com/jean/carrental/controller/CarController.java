package com.jean.carrental.controller;

import com.jean.carrental.service.CarService;
import com.jean.carrental.dto.CarDTO;
import com.jean.carrental.model.Car;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public List<CarDTO> getAllCars() {
        log.info("Received request to fetch all cars");
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public CarDTO getCarById(@PathVariable int id) {
        log.info("Received request to fetch car with id: {}", id);
        return carService.getCarById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO addCar(@Valid @RequestBody Car car) {
        log.info("Received request to create a new car with make: {} and model: {}", car.getMake(), car.getModel());
        return carService.addCar(car);
    }

    @PutMapping("/{id}")
    public CarDTO updateCar(@PathVariable int id, @Valid @RequestBody Car car) {
        log.info("Received request to update car with id: {}", id);
        return carService.updateCar(id, car);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable int id) {
        log.info("Received request to delete car with id: {}", id);
        carService.deleteCar(id);
    }
}
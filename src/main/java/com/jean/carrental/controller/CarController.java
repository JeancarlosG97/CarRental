package com.jean.carrental.controller;

import com.jean.carrental.service.CarService;
import com.jean.carrental.dto.CarDTO;
import com.jean.carrental.model.Car;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public List<CarDTO> getAllCars() {
        return carService.getAllCars();
    }

    @GetMapping("/{id}")
    public CarDTO getCarById(@PathVariable int id) {
        return carService.getCarById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO addCar(@Valid @RequestBody Car car) {
        return carService.addCar(car);
    }

    @PutMapping("/{id}")
    public CarDTO updateCar(@PathVariable int id, @Valid @RequestBody Car car) {
        return carService.updateCar(id, car);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable int id) {
        carService.deleteCar(id);
    }
}
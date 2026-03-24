package com.jean.carrental.service;

import com.jean.carrental.exception.CarNotFoundException;
import com.jean.carrental.repository.CarRepository;
import com.jean.carrental.dto.CarDTO;
import com.jean.carrental.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<CarDTO> getAllCars() {
        log.info("Fetching all cars");

        return carRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CarDTO getCarById(int id) {
        log.info("Fetching car with id: {}", id);

        Car car = carRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Car with id {} not found", id);
                    return new CarNotFoundException(id);
                });
        return convertToDTO(car);
    }

    public CarDTO addCar(Car car) {
        log.info("Request to create car | Make: {}, Model: {}, Year: {}, PricePerDay: {}", car.getMake(), car.getModel(), car.getYear(), car.getPricePerDay());

        Car savedCar = carRepository.save(car);
        log.info("Car created successfully with id: {}", savedCar.getId());
        return convertToDTO(savedCar);
    }

    public CarDTO updateCar(int id, Car updatedCar) {
        log.info("Updating car with id: {}", id);

        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Car with id {} not found for update", id);
                    return new CarNotFoundException(id);
                });

        existingCar.setMake(updatedCar.getMake());
        existingCar.setModel(updatedCar.getModel());
        existingCar.setYear(updatedCar.getYear());
        existingCar.setPricePerDay(updatedCar.getPricePerDay());
        existingCar.setAvailable(updatedCar.isAvailable());

        Car savedCar = carRepository.save(existingCar);
        log.info("Car with id {} updated successfully (make: {}, model: {})", savedCar.getId(), savedCar.getMake(), savedCar.getModel());
        return convertToDTO(savedCar);
    }

    public void deleteCar(int id) {
        log.info("Attempting to delete a car with id: {}", id);
        Car car = carRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Car deletion with id {} was not found", id);
                    return new CarNotFoundException(id);
                });

        carRepository.delete(car);
        log.info("Car with id {} was successfully deleted", id);
    }

    public CarDTO convertToDTO(Car car) {
        return new CarDTO(
                car.getId(),
                car.getMake(),
                car.getModel(),
                car.getYear(),
                car.getPricePerDay(),
                car.isAvailable()
        );
    }
}
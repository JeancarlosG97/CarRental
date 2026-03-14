package com.jean.carrental.Service;

import com.jean.carrental.Exception.CarNotFoundException;
import com.jean.carrental.Repository.CarRepository;
import com.jean.carrental.dto.CarDTO;
import com.jean.carrental.model.Car;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<CarDTO> getAllCars() {
        return carRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public CarDTO getCarById(int id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        return convertToDTO(car);
    }

    public CarDTO addCar(Car car) {
        Car savedCar = carRepository.save(car);
        return convertToDTO(savedCar);
    }

    public CarDTO updateCar(int id, Car updatedCar) {
        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        existingCar.setMake(updatedCar.getMake());
        existingCar.setModel(updatedCar.getModel());
        existingCar.setYear(updatedCar.getYear());
        existingCar.setPricePerDay(updatedCar.getPricePerDay());
        existingCar.setAvailable(updatedCar.isAvailable());

        Car savedCar = carRepository.save(existingCar);
        return convertToDTO(savedCar);
    }

    public void deleteCar(int id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        carRepository.delete(car);
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
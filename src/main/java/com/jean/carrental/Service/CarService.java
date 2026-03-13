package com.jean.carrental.Service;

import com.jean.carrental.Exception.CarNotFoundException;
import com.jean.carrental.Repository.CarRepository;
import com.jean.carrental.model.Car;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(int id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
    }

    public Car addCar(Car car) {
        return carRepository.save(car);
    }

    public Car updateCar(int id, Car updatedCar) {
        Car existingCar = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        existingCar.setMake(updatedCar.getMake());
        existingCar.setModel(updatedCar.getModel());
        existingCar.setYear(updatedCar.getYear());
        existingCar.setPricePerDay(updatedCar.getPricePerDay());
        existingCar.setAvailable(updatedCar.isAvailable());

        return carRepository.save(existingCar);
    }

    public void deleteCar(int id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        carRepository.delete(car);
    }
}
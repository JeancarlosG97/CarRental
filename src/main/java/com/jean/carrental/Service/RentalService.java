package com.jean.carrental.Service;

import com.jean.carrental.Exception.RentalNotFoundException;
import com.jean.carrental.Repository.CarRepository;
import com.jean.carrental.Repository.RentalRepository;
import com.jean.carrental.model.Car;
import com.jean.carrental.model.Rental;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;

    public RentalService(RentalRepository rentalRepository, CarRepository carRepository) {
        this.rentalRepository = rentalRepository;
        this.carRepository = carRepository;
    }

    public List<Rental> getAllRentals() {
        return rentalRepository.findAll();
    }

    public Rental getRentalById(int id) {
        return rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
    }

    public Rental addRental(Rental rental) {

        Car car = rental.getCar();

        if (!car.isAvailable()) {
            throw new RuntimeException("Car is not available to rent at the moment.");
        }

        car.setAvailable(false);
        carRepository.save(car);

        rental.setReturned(false);
        return rentalRepository.save(rental);
    }

    public Rental updateRental(int id, Rental updatedRental) {
        Rental existingRental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));

        existingRental.setCar(updatedRental.getCar());
        existingRental.setCustomer(updatedRental.getCustomer());
        existingRental.setRentalDate(updatedRental.getRentalDate());
        existingRental.setReturnDate(updatedRental.getReturnDate());
        existingRental.setPrice(updatedRental.getPrice());
        existingRental.setReturned(updatedRental.isReturned());

        return rentalRepository.save(existingRental);
    }

    public void deleteRental(int id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));

        Car car = rental.getCar();
        car.setAvailable(true);
        carRepository.save(car);

        rentalRepository.delete(rental);
    }
}
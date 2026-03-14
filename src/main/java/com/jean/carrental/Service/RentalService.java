package com.jean.carrental.Service;

import com.jean.carrental.Exception.CarNotFoundException;
import com.jean.carrental.Exception.CustomerNotFoundException;
import com.jean.carrental.Exception.RentalNotFoundException;
import com.jean.carrental.Repository.CarRepository;
import com.jean.carrental.Repository.CustomerRepository;
import com.jean.carrental.Repository.RentalRepository;
import com.jean.carrental.dto.RentalDTO;
import com.jean.carrental.model.Car;
import com.jean.carrental.model.Customer;
import com.jean.carrental.model.Rental;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;

    public RentalService(RentalRepository rentalRepository, CarRepository carRepository, CustomerRepository customerRepository) {
        this.rentalRepository = rentalRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
    }

    public List<RentalDTO> getAllRentals() {
        return rentalRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public RentalDTO getRentalById(int id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));
        return convertToDTO(rental);
    }

    public RentalDTO addRental(Rental rental) {

        Car car = carRepository.findById(rental.getCar().getId())
                .orElseThrow(() -> new CarNotFoundException(rental.getCar().getId()));

        Customer customer = customerRepository.findById(rental.getCustomer().getId())
                .orElseThrow(() -> new CustomerNotFoundException(rental.getCustomer().getId()));

        if (!car.isAvailable()) {
            throw new RuntimeException("Car is not available to rent at the moment.");
        }

        car.setAvailable(false);
        carRepository.save(car);

        rental.setCar(car);
        rental.setCustomer(customer);
        rental.setReturned(false);

        Rental savedRental = rentalRepository.save(rental);
        return convertToDTO(savedRental);
    }

    public RentalDTO updateRental(int id, Rental updatedRental) {
        Rental existingRental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));

        Car car = carRepository.findById(updatedRental.getCar().getId())
                .orElseThrow(() -> new CarNotFoundException(updatedRental.getCar().getId()));

        Customer customer = customerRepository.findById(updatedRental.getCustomer().getId())
                .orElseThrow(() -> new CustomerNotFoundException(updatedRental.getCustomer().getId()));

        existingRental.setCar(car);
        existingRental.setCustomer(customer);
        existingRental.setRentalDate(updatedRental.getRentalDate());
        existingRental.setReturnDate(updatedRental.getReturnDate());
        existingRental.setPrice(updatedRental.getPrice());
        existingRental.setReturned(updatedRental.isReturned());

        Rental savedRental = rentalRepository.save(existingRental);
        return convertToDTO(savedRental);
    }

    public void deleteRental(int id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));

        Car car = rental.getCar();
        car.setAvailable(true);
        carRepository.save(car);

        rentalRepository.delete(rental);
    }

    private RentalDTO convertToDTO(Rental rental) {
        return new RentalDTO(
                rental.getId(),
                rental.getCustomer().getName(),
                rental.getCustomer().getEmail(),
                rental.getCar().getMake(),
                rental.getCar().getModel(),
                rental.getRentalDate(),
                rental.getReturnDate(),
                rental.getPrice(),
                rental.isReturned()
        );
    }
}
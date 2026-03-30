package com.jean.carrental.service;

import com.jean.carrental.exception.CarNotFoundException;
import com.jean.carrental.exception.CustomerNotFoundException;
import com.jean.carrental.exception.RentalNotFoundException;
import com.jean.carrental.repository.CarRepository;
import com.jean.carrental.repository.CustomerRepository;
import com.jean.carrental.repository.RentalRepository;
import com.jean.carrental.dto.RentalDTO;
import com.jean.carrental.model.Car;
import com.jean.carrental.model.Customer;
import com.jean.carrental.model.Rental;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.Security;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RentalService {

    private final RentalRepository rentalRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;

    public RentalService(RentalRepository rentalRepository, CarRepository carRepository, CustomerRepository customerRepository) {
        this.rentalRepository = rentalRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
    }

    // Get all rentals on file
    public List<RentalDTO> getAllRentals() {
        log.info("Fetching all rentals");

        return rentalRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get rentals by id
    public RentalDTO getRentalById(int id) {
        log.info("Fetching rentals with id: {}", id);

        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Rental with id {} not found", id);
                    return new RentalNotFoundException(id);
                });
        return convertToDTO(rental);
    }

    // Add a new rental
    public RentalDTO addRental(Rental rental) {
        log.info("Creating rental for customer {} and car {}", rental.getCustomer().getId(), rental.getCar().getId());

        Car car = carRepository.findById(rental.getCar().getId())
                .orElseThrow(() -> {
                    log.warn("Car with id {} not found", rental.getCar().getId());
                    return new CarNotFoundException(rental.getCar().getId());
                });

        Customer customer = customerRepository.findById(rental.getCustomer().getId())
                .orElseThrow(() -> {
                    log.warn("Customer with id {} not found", rental.getCustomer().getId());
                    return new CustomerNotFoundException(rental.getCustomer().getId());
                });

        if (!car.isAvailable()) {
            log.info("Car with id {} was not available to rent by customer with id {}", car.getId(), customer.getId());
            throw new RuntimeException("Car is not available to rent at the moment.");
        }

        car.setAvailable(false);
        carRepository.save(car);
        log.info("Car with id {} set to not available", car.getId());

        rental.setCar(car);
        rental.setCustomer(customer);
        rental.setReturned(false);

        Rental savedRental = rentalRepository.save(rental);
        log.info("Rental created successfully with id {}, customer {}, car id {}", savedRental.getId(), savedRental.getCustomer().getId(), savedRental.getCar().getId());
        return convertToDTO(savedRental);
    }

    // Update an existing rental found by id
    public RentalDTO updateRental(int id, Rental updatedRental) {
        log.info("Updating rental with id: {}", id);

        Rental existingRental = rentalRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Rental with id {} not found for update", id);
                    return new RentalNotFoundException(id);
                });

        Car car = carRepository.findById(updatedRental.getCar().getId())
                .orElseThrow(() -> {
                    log.warn("Car with id {} not found during rental update", updatedRental.getCar().getId());
                    return new CarNotFoundException(updatedRental.getCar().getId());
                });

        Customer customer = customerRepository.findById(updatedRental.getCustomer().getId())
                .orElseThrow(() -> {
                    log.warn("Customer with id {} not found during rental update", updatedRental.getCustomer().getId());
                    return new CustomerNotFoundException(updatedRental.getCustomer().getId());
                });

        existingRental.setCar(car);
        existingRental.setCustomer(customer);
        existingRental.setRentalDate(updatedRental.getRentalDate());
        existingRental.setReturnDate(updatedRental.getReturnDate());
        existingRental.setPrice(updatedRental.getPrice());
        existingRental.setReturned(updatedRental.isReturned());

        Rental savedRental = rentalRepository.save(existingRental);
        log.info("Rental with id {} updated successfully", savedRental.getId());
        return convertToDTO(savedRental);
    }

    // Delete a rental
    public void deleteRental(int id) {
        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Rental deletion with id {} not found", id);
                    return new RentalNotFoundException(id);
                });

        Car car = rental.getCar();
        car.setAvailable(true);
        carRepository.save(car);

        rentalRepository.delete(rental);
        log.info("Rental with id {} deleted successfully", id);
    }

    // Converting entity info to DTO
    public RentalDTO convertToDTO(Rental rental) {
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

    // Rent a car
    public RentalDTO rentCar(int customerId, int carId, int rentalDays) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        if (!car.isAvailable()) {
            throw new RuntimeException("Car is not available to rent");
        }

        Rental newRental = new Rental();
        newRental.setCar(car);
        newRental.setCustomer(customer);

        LocalDate today = LocalDate.now();
        newRental.setRentalDate(today);

        LocalDate returnDate = today.plusDays(rentalDays);
        newRental.setReturnDate(returnDate);

        double totalPrice = car.getPricePerDay() * rentalDays;
        newRental.setPrice(totalPrice);

        newRental.setReturned(false);
        car.setAvailable(false);

        carRepository.save(car);
        rentalRepository.save(newRental);

        return convertToDTO(newRental);
    }

    // Return a car
    public RentalDTO returnRental(int id) {

        Rental rental = rentalRepository.findById(id)
                .orElseThrow(() -> new RentalNotFoundException(id));

        rental.setReturned(true);

        Car car = rental.getCar();
        car.setAvailable(true);

        carRepository.save(car);

        Rental updatedRental = rentalRepository.save(rental);
        return convertToDTO(updatedRental);
    }

    public List<RentalDTO> getMyRentals() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new CustomerNotFoundException(email));

        List<Rental> rentals = rentalRepository.findByCustomer(customer);

        return rentals.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
}
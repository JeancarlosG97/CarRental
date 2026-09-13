package com.jean.carrental.service;

import com.jean.carrental.dto.RentalDTO;
import com.jean.carrental.exception.CarNotFoundException;
import com.jean.carrental.exception.CustomerNotFoundException;
import com.jean.carrental.exception.RentalNotFoundException;
import com.jean.carrental.model.Car;
import com.jean.carrental.model.Customer;
import com.jean.carrental.model.Rental;
import com.jean.carrental.repository.CarRepository;
import com.jean.carrental.repository.CustomerRepository;
import com.jean.carrental.repository.RentalRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private RentalService rentalService;

    @Test
    void shouldReturnAllRentals() {

        Customer customer = new Customer();
        customer.setName("Jean");
        customer.setEmail("jean@jean.com");

        Car car = new Car();
        car.setMake("Toyota");
        car.setModel("Camry");

        Rental r1 = new Rental();
        r1.setId(1);
        r1.setCustomer(customer);
        r1.setCar(car);

        Rental r2 = new Rental();
        r2.setId(2);
        r2.setCustomer(customer);
        r2.setCar(car);

        when(rentalRepository.findAll())
                .thenReturn(List.of(r1, r2));

        List<RentalDTO> result =
                rentalService.getAllRentals();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getRentalID());
        assertEquals(2, result.get(1).getRentalID());

        verify(rentalRepository).findAll();
    }

    @Test
    void shouldReturnRentalWhenRentalExists() {

        Customer customer = new Customer();
        customer.setName("Jean");
        customer.setEmail("jean@jean.com");

        Car car = new Car();
        car.setMake("Toyota");
        car.setModel("Camry");

        Rental rental = new Rental();
        rental.setId(1);
        rental.setCustomer(customer);
        rental.setCar(car);

        when(rentalRepository.findById(1))
                .thenReturn(Optional.of(rental));

        RentalDTO result =
                rentalService.getRentalById(1);

        assertEquals(1, result.getRentalID());
        assertEquals("Jean", result.getCustomerName());
        assertEquals("Toyota", result.getCarMake());

        verify(rentalRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenRentalNotFound() {

        when(rentalRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                RentalNotFoundException.class,
                () -> rentalService.getRentalById(1)
        );

        verify(rentalRepository).findById(1);
    }

    @Test
    void shouldAddRentalWhenCarAndCustomerExist() {

        Car car = new Car();
        car.setId(1);
        car.setAvailable(true);
        car.setMake("Toyota");

        Customer customer = new Customer();
        customer.setId(1);
        customer.setName("Jean");

        Rental rental = new Rental();
        rental.setCar(car);
        rental.setCustomer(customer);

        when(carRepository.findById(1))
                .thenReturn(Optional.of(car));

        when(customerRepository.findById(1))
                .thenReturn(Optional.of(customer));

        when(carRepository.save(car))
                .thenReturn(car);

        when(rentalRepository.save(rental))
                .thenReturn(rental);

        RentalDTO result =
                rentalService.addRental(rental);

        assertEquals(false, rental.isReturned());
        assertEquals(false, car.isAvailable());

        verify(carRepository).findById(1);
        verify(customerRepository).findById(1);
        verify(carRepository).save(car);
        verify(rentalRepository).save(rental);
    }

    @Test
    void shouldThrowExceptionWhenCarNotFound() {

        Car car = new Car();
        car.setId(1);

        Customer customer = new Customer();
        customer.setId(1);

        Rental rental = new Rental();
        rental.setCar(car);
        rental.setCustomer(customer);

        when(carRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CarNotFoundException.class,
                () -> rentalService.addRental(rental)
        );

        verify(carRepository).findById(1);
        verify(rentalRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFound() {

        Car car = new Car();
        car.setId(1);

        Customer customer = new Customer();
        customer.setId(1);

        Rental rental = new Rental();
        rental.setCar(car);
        rental.setCustomer(customer);

        when(carRepository.findById(1))
                .thenReturn(Optional.of(car));

        when(customerRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class,
                () -> rentalService.addRental(rental)
        );

        verify(customerRepository).findById(1);
        verify(rentalRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCarIsUnavailable() {

        Car car = new Car();
        car.setId(1);
        car.setAvailable(false);

        Customer customer = new Customer();
        customer.setId(1);

        Rental rental = new Rental();
        rental.setCar(car);
        rental.setCustomer(customer);

        when(carRepository.findById(1))
                .thenReturn(Optional.of(car));

        when(customerRepository.findById(1))
                .thenReturn(Optional.of(customer));

        assertThrows(
                RuntimeException.class,
                () -> rentalService.addRental(rental)
        );

        verify(rentalRepository, never()).save(any());
    }

    @Test
    void shouldUpdateRentalWhenRentalExists() {

        Car car = new Car();
        car.setId(1);

        Customer customer = new Customer();
        customer.setId(1);

        Rental existingRental = new Rental();
        existingRental.setId(1);

        Rental updatedRental = new Rental();
        updatedRental.setCar(car);
        updatedRental.setCustomer(customer);
        updatedRental.setPrice(500);
        updatedRental.setReturned(true);

        LocalDate rentalDate = LocalDate.now();
        LocalDate returnDate = LocalDate.now().plusDays(5);

        updatedRental.setRentalDate(rentalDate);
        updatedRental.setReturnDate(returnDate);

        when(rentalRepository.findById(1))
                .thenReturn(Optional.of(existingRental));

        when(carRepository.findById(1))
                .thenReturn(Optional.of(car));

        when(customerRepository.findById(1))
                .thenReturn(Optional.of(customer));

        when(rentalRepository.save(existingRental))
                .thenReturn(existingRental);

        RentalDTO result =
                rentalService.updateRental(1, updatedRental);

        assertEquals(500, result.getPrice());
        assertEquals(true, result.isReturned());

        verify(rentalRepository).findById(1);
        verify(rentalRepository).save(existingRental);
    }

    @Test
    void shouldThrowExceptionWhenRentalNotFoundDuringUpdate() {

        when(rentalRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                RentalNotFoundException.class,
                () -> rentalService.updateRental(1, new Rental())
        );

        verify(rentalRepository).findById(1);
        verify(rentalRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenCarNotFoundDuringUpdate() {

        Rental existingRental = new Rental();

        Car car = new Car();
        car.setId(1);

        Customer customer = new Customer();
        customer.setId(1);

        Rental updatedRental = new Rental();
        updatedRental.setCar(car);
        updatedRental.setCustomer(customer);

        when(rentalRepository.findById(1))
                .thenReturn(Optional.of(existingRental));

        when(carRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CarNotFoundException.class,
                () -> rentalService.updateRental(1, updatedRental)
        );
    }

    @Test
    void shouldThrowExceptionWhenCustomerNotFoundDuringUpdate() {

        Rental existingRental = new Rental();

        Car car = new Car();
        car.setId(1);

        Customer customer = new Customer();
        customer.setId(1);

        Rental updatedRental = new Rental();
        updatedRental.setCar(car);
        updatedRental.setCustomer(customer);

        when(rentalRepository.findById(1))
                .thenReturn(Optional.of(existingRental));

        when(carRepository.findById(1))
                .thenReturn(Optional.of(car));

        when(customerRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CustomerNotFoundException.class,
                () -> rentalService.updateRental(1, updatedRental)
        );
    }

    @Test
    void shouldDeleteRentalWhenRentalExists() {

        Car car = new Car();
        car.setAvailable(false);

        Rental rental = new Rental();
        rental.setId(1);
        rental.setCar(car);

        when(rentalRepository.findById(1))
                .thenReturn(Optional.of(rental));

        rentalService.deleteRental(1);

        verify(rentalRepository).findById(1);
        verify(carRepository).save(car);
        verify(rentalRepository).delete(rental);

        assertEquals(true, car.isAvailable());
    }

    @Test
    void shouldThrowExceptionWhenDeletingMissingRental() {

        when(rentalRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                RentalNotFoundException.class,
                () -> rentalService.deleteRental(1)
        );

        verify(rentalRepository).findById(1);
        verify(rentalRepository, never()).delete(any());
    }
}
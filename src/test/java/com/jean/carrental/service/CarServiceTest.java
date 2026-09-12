package com.jean.carrental.service;

import com.jean.carrental.dto.CarDTO;
import com.jean.carrental.exception.CarNotFoundException;
import com.jean.carrental.model.Car;
import com.jean.carrental.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @Test
    void shouldReturnAllCars() {

        Car car1 = new Car();
        car1.setId(1);
        car1.setMake("Toyota");
        car1.setModel("Camry");

        Car car2 = new Car();
        car2.setId(2);
        car2.setMake("Honda");
        car2.setModel("Civic");

        when(carRepository.findAll())
                .thenReturn(List.of(car1, car2));

        List<CarDTO> result = carService.getAllCars();

        assertEquals(2, result.size());
        assertEquals("Toyota", result.get(0).getMake());
        assertEquals("Honda", result.get(1).getMake());

        verify(carRepository).findAll();
    }

    @Test
    void shouldReturnCarWhenCarExists() {

        Car car = new Car();
        car.setId(1);
        car.setMake("Toyota");

        when(carRepository.findById(1))
                .thenReturn(Optional.of(car));

        CarDTO result = carService.getCarById(1);

        assertEquals(1, result.getId());
        assertEquals("Toyota", result.getMake());

        verify(carRepository).findById(1);
    }

    @Test
    void shouldThrowExceptionWhenCarNotFound() {

        when(carRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CarNotFoundException.class,
                () -> carService.getCarById(1)
        );

        verify(carRepository).findById(1);
    }

    @Test
    void shouldAddCar() {

        Car car = new Car();
        car.setMake("Toyota");
        car.setModel("Camry");

        Car savedCar = new Car();
        savedCar.setId(1);
        savedCar.setMake("Toyota");
        savedCar.setModel("Camry");

        when(carRepository.save(car))
                .thenReturn(savedCar);

        CarDTO result = carService.addCar(car);

        assertEquals(1, result.getId());
        assertEquals("Toyota", result.getMake());
        assertEquals("Camry", result.getModel());

        verify(carRepository).save(car);
    }

    @Test
    void shouldUpdateCarWhenCarExists() {

        Car existingCar = new Car();
        existingCar.setId(1);
        existingCar.setMake("Toyota");
        existingCar.setModel("Camry");

        Car updatedCar = new Car();
        updatedCar.setMake("Honda");
        updatedCar.setModel("Civic");

        when(carRepository.findById(1))
                .thenReturn(Optional.of(existingCar));

        when(carRepository.save(existingCar))
                .thenReturn(existingCar);

        CarDTO result = carService.updateCar(1, updatedCar);

        assertEquals("Honda", result.getMake());
        assertEquals("Civic", result.getModel());

        verify(carRepository).findById(1);
        verify(carRepository).save(existingCar);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingMissingCar() {

        Car updatedCar = new Car();
        updatedCar.setModel("Honda");
        updatedCar.setMake("Civic");

        when(carRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CarNotFoundException.class,
                () -> carService.updateCar(1, updatedCar)
        );

        verify(carRepository).findById(1);
    }

    @Test
    void shouldDeleteCarWhenCarExists() {

        Car car = new Car();
        car.setId(1);
        car.setMake("Toyota");

        when(carRepository.findById(1))
                .thenReturn(Optional.of(car));

        carService.deleteCar(1);

        verify(carRepository).findById(1);
        verify(carRepository).delete(car);
    }

    @Test
    void shouldThrowExceptionWhenDeletingMissingCar() {

        when(carRepository.findById(1))
                .thenReturn(Optional.empty());

        assertThrows(
                CarNotFoundException.class,
                () -> carService.deleteCar(1)
        );

        verify(carRepository, never())
                .delete(any());
    }
}

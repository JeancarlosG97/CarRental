package com.jean.carrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jean.carrental.dto.CarDTO;
import com.jean.carrental.exception.CarNotFoundException;
import com.jean.carrental.model.Car;
import com.jean.carrental.security.JwtRequestFilter;
import com.jean.carrental.security.JwtUtil;
import com.jean.carrental.service.CarService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CarController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CarService carService;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @WithMockUser(
            username = "test@test.com",
            roles = {"USER"}
    )
    void shouldReturnCarWhenCarExists() throws Exception {

        CarDTO carDTO = new CarDTO(
                1,
                "Toyota",
                "Camry",
                2024,
                50.0,
                true
        );

        when(carService.getCarById(1))
                .thenReturn(carDTO);

        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.make").value("Toyota"))
                .andExpect(jsonPath("$.model").value("Camry"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = {"USER"})
    void shouldReturnAllCars() throws Exception {

        // Arrange
        List<CarDTO> cars = List.of(
                new CarDTO(1, "Toyota", "Camry", 2024, 50.0, true),
                new CarDTO(2, "Honda", "Civic", 2023, 45.0, true)
        );

        when(carService.getAllCars())
                .thenReturn(cars);

        // Act & Assert
        mockMvc.perform(get("/cars"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].make").value("Toyota"))
                .andExpect(jsonPath("$[1].make").value("Honda"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = {"USER"})
    void shouldReturnNotFoundWhenCarDoesNotExist() throws Exception {

        when(carService.getCarById(999))
                .thenThrow(new CarNotFoundException(999));

        mockMvc.perform(get("/cars/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = {"ADMIN"})
    void shouldDeleteCar() throws Exception {

        mockMvc.perform(delete("/cars/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = {"ADMIN"})
    void shouldCreateCar() throws Exception {

        Car request = new Car();
        request.setMake("Toyota");
        request.setModel("Camry");
        request.setYear(2024);
        request.setPricePerDay(50.0);
        request.setAvailable(true);

        CarDTO response = new CarDTO(
                1,
                "Toyota",
                "Camry",
                2024,
                50.0,
                true
        );

        when(carService.addCar(any(Car.class)))
                .thenReturn(response);

        mockMvc.perform(post("/cars")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.make").value("Toyota"));
    }

    @Test
    @WithMockUser(username = "test@test.com", roles = {"ADMIN"})
    void shouldUpdateCar() throws Exception {

        Car request = new Car();
        request.setMake("Honda");
        request.setModel("Civic");
        request.setYear(2024);
        request.setPricePerDay(55.0);
        request.setAvailable(true);

        CarDTO response = new CarDTO(
                1,
                "Honda",
                "Civic",
                2024,
                55.0,
                true
        );

        when(carService.updateCar(eq(1), any(Car.class)))
                .thenReturn(response);

        mockMvc.perform(put("/cars/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.make").value("Honda"))
                .andExpect(jsonPath("$.model").value("Civic"));
    }
}
package com.jean.carrental.Repository;

import com.jean.carrental.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository  extends JpaRepository<Car, Integer> {
}

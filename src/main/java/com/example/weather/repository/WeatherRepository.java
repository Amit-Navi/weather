package com.example.weather.repository;

import com.example.weather.model.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface WeatherRepository extends JpaRepository<WeatherInfo, Long> {
    Optional<WeatherInfo> findByPincodeAndDate(String pincode, LocalDate date);
}

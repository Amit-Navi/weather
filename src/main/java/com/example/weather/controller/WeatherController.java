package com.example.weather.controller;

import com.example.weather.model.WeatherInfo;
import com.example.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public WeatherInfo getWeather(@RequestParam String pincode, @RequestParam String date) {
        return weatherService.getWeather(pincode, LocalDate.parse(date));
    }
}

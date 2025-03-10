package com.example.weather.service;

import com.example.weather.model.WeatherInfo;
import com.example.weather.repository.WeatherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class WeatherService {
    private static final String API_KEY = "YOUR_OPENWEATHERMAP_API_KEY";
    private static final String GEO_API = "https://api.openweathermap.org/geo/1.0/direct?q=%s&limit=1&appid=" + API_KEY;
    private static final String WEATHER_API = "https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=" + API_KEY;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    public RestTemplate restTemplate;

    public WeatherInfo getWeather(String pincode, LocalDate date) {
        Optional<WeatherInfo> existingWeather = weatherRepository.findByPincodeAndDate(pincode, date);
        if (existingWeather.isPresent()) {
            return existingWeather.get();
        }

        // Fetch latitude & longitude from OpenWeather Geocoding API
        String geoUrl = String.format(GEO_API, pincode);
        Object[] geoResponse = restTemplate.getForObject(geoUrl, Object[].class);
        if (geoResponse == null || geoResponse.length == 0) {
            throw new RuntimeException("Invalid Pincode");
        }
        double lat = (double) ((java.util.Map) geoResponse[0]).get("lat");
        double lon = (double) ((java.util.Map) geoResponse[0]).get("lon");
        // Fetch weather data
        String weatherUrl = String.format(WEATHER_API, lat, lon);
        String weatherResponse = restTemplate.getForObject(weatherUrl, String.class);

        WeatherInfo weatherInfo = new WeatherInfo(null, pincode, lat, lon, date, weatherResponse);
        return weatherRepository.save(weatherInfo);
    }
}

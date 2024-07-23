package com.silvan.slvweatherclient;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@SpringBootApplication
public class SlvWeatherClientApplication {
    public static void main(String[] args) {
        String sensorName = "FirstSensor";

        registerSensor(sensorName);

        Random random = new Random();

        double minTemperature = -100.0;
        double maxTemperature = 100.0;

        for (int i = 0; i < 1000; i++){
            System.out.println(i);
            sendMeasurement(random.nextDouble() * maxTemperature,
                    random.nextBoolean(),
                    sensorName);
        }
    }
    public static void registerSensor(String sensorName){
        String url = "http://localhost:8080/sensors/register";

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("name", sensorName);

        postJSON(url, jsonData);
    }

    public static void sendMeasurement(double value,
                                       boolean raining,
                                       String sensorName){

        Map<String, Object> jsonData = new HashMap<>();
        jsonData.put("value",value);
        jsonData.put("raining", raining);
        jsonData.put("sensor", Map.of("name", sensorName));
    }

    public static void postJSON(String url, Map<String, Object> jsonData){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);

        try {
        restTemplate.postForObject(url, request, String.class);

        System.out.println("Измерение успешно отправлено!");
    } catch (HttpClientErrorException exception) {
            System.out.println("Ошибка при отправке измерения!");
            System.out.println(exception.getMessage());
        }
    }
}


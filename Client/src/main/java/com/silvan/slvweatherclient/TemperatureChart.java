package com.silvan.slvweatherclient;

import com.silvan.slvweatherclient.dto.MeasurementDTO;
import com.silvan.slvweatherclient.util.MeasurementResponse;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TemperatureChart {
    public static void main(String[] args) {
        List<Double> temperatures = getTemperatures();
        drawChart(temperatures);
    }

    public static List<Double> getTemperatures(){
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8080/measurements";

        MeasurementResponse response = restTemplate.getForObject(url, MeasurementResponse.class);

        if (response == null || response.getMeasurements() == null)
            return Collections.emptyList();

        return response.getMeasurements()
                .stream().map(MeasurementDTO::getValue)
                .collect(Collectors.toList());
    }
    public static void drawChart(List<Double> temperatures){
        double[] xAxis = IntStream.range(0, temperatures.size()).asDoubleStream().toArray();
        double[] yAxis = temperatures.stream().mapToDouble(x -> x).toArray();

        XYChart chart = QuickChart.getChart("Temperatures", "X-Axis", "Y-Axis", "Temperature", xAxis,yAxis);

        new SwingWrapper<>(chart).displayChart();
    }
}

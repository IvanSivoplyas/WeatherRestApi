package ru.silvan.springcourse.FirstRestApp.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.silvan.springcourse.FirstRestApp.models.Sensor;
import ru.silvan.springcourse.FirstRestApp.repositories.SensorRepository;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class SensorService {
    private final SensorRepository sensorRepository;

    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }
    public Optional<Sensor> findByName(String name){
        return sensorRepository.findByName(name);
    }

    public void register(Sensor sensor){
        sensorRepository.save(sensor);
    }
}

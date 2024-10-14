package ru.silvan.springcourse.FirstRestApp.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.silvan.springcourse.FirstRestApp.dto.SensorDTO;
import ru.silvan.springcourse.FirstRestApp.models.Sensor;
import ru.silvan.springcourse.FirstRestApp.services.SensorService;
import ru.silvan.springcourse.FirstRestApp.util.MeasurementErrorResponse;
import ru.silvan.springcourse.FirstRestApp.util.MeasurementException;
import ru.silvan.springcourse.FirstRestApp.util.SensorValidator;

import static ru.silvan.springcourse.FirstRestApp.util.Errors.returnErrorsToClient;

@RestController
@RequestMapping("/sensors")
public class SensorController {
    private final SensorService sensorService;
    private final ModelMapper modelMapper;
    private final SensorValidator sensorValidator;

    public SensorController(SensorService sensorService,
                            ModelMapper modelMapper,
                            SensorValidator sensorValidator) {
        this.sensorService = sensorService;
        this.modelMapper = modelMapper;
        this.sensorValidator = sensorValidator;
    }

    @PostMapping("/register")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid SensorDTO sensorDTO,
                                               BindingResult bindingResult){
        Sensor addedSensor = addSensor(sensorDTO);

        sensorValidator.validate(addedSensor,bindingResult);

        if (bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        sensorService.register(addedSensor);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException exception){
        MeasurementErrorResponse response = new MeasurementErrorResponse(exception.getMessage(), System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    private Sensor addSensor(SensorDTO sensorDTO){
        return  modelMapper.map(sensorDTO, Sensor.class);
    }
}

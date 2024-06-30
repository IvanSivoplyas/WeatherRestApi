package ru.silvan.springcourse.FirstRestApp.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.silvan.springcourse.FirstRestApp.dto.MeasurementDTO;
import ru.silvan.springcourse.FirstRestApp.models.Measurement;
import ru.silvan.springcourse.FirstRestApp.services.MeasurementService;
import ru.silvan.springcourse.FirstRestApp.util.MeasurementErrorResponse;
import ru.silvan.springcourse.FirstRestApp.util.MeasurementException;
import ru.silvan.springcourse.FirstRestApp.util.MeasurementResponse;
import ru.silvan.springcourse.FirstRestApp.util.MeasurementValidator;

import java.util.stream.Collectors;

import static ru.silvan.springcourse.FirstRestApp.util.Errors.returnErrorsToClient;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {
    private final MeasurementService measurementService;
    private final MeasurementValidator measurementValidator;
    private final ModelMapper modelMapper;

    public MeasurementController(MeasurementService measurementService,
                                 MeasurementValidator measurementValidator,
                                 ModelMapper modelMapper) {
        this.measurementService = measurementService;
        this.measurementValidator = measurementValidator;
        this.modelMapper = modelMapper;
    }

    @PostMapping("add")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid MeasurementDTO measurementDTO, BindingResult bindingResult){
        Measurement addedMeasurement = addMeasurement(measurementDTO);

        measurementValidator.validate(addedMeasurement,bindingResult);
        if (bindingResult.hasErrors())
            returnErrorsToClient(bindingResult);

        measurementService.addMeasurement(addedMeasurement);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @GetMapping
    public MeasurementResponse getMeasurements(){
        return new MeasurementResponse(measurementService.findAll().stream().map(this::addMeasurementDTO).collect(Collectors.toList()));
    }
    @GetMapping("/rainy-days")
    private Long getRainyDaysCount(){
        return measurementService.findAll().stream().filter(Measurement::getRaining).count();
    }
    private Measurement addMeasurement(MeasurementDTO measurementDTO){
        return modelMapper.map(measurementDTO, Measurement.class);
    }
    private MeasurementDTO addMeasurementDTO(Measurement measurement){
        return modelMapper.map(measurement, MeasurementDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<MeasurementErrorResponse> handleException(MeasurementException exception){
        MeasurementErrorResponse response = new MeasurementErrorResponse(
          exception.getMessage(),System.currentTimeMillis()
        );
        return new ResponseEntity<>(response,HttpStatus.BAD_REQUEST);
    }
}

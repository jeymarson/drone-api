package com.musala.drone.controllers;

import com.musala.drone.constants.ResponseMessage;
import com.musala.drone.dtos.CreateOperationDTO;
import com.musala.drone.dtos.DroneWithMedsDTO;
import com.musala.drone.dtos.FinishOperationDTO;
import com.musala.drone.dtos.ResponseDTO;
import com.musala.drone.models.Operation;
import com.musala.drone.services.OperationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("operation")
public class OperationController {

    private final OperationService operationService;

    public OperationController(OperationService operationService) {
        this.operationService = operationService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getOperationById(@PathVariable("id") Long id) {
        Operation operation = this.operationService.getOperationById(id);
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.OK.value(), ResponseMessage.SUCCESSFULLY_QUERY, operation, null), HttpStatus.OK);
    }

    @GetMapping("/open")
    public ResponseEntity<ResponseDTO> getOpenOperations() {
        List<Operation> operation = this.operationService.getOpenOperations();
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.OK.value(), ResponseMessage.SUCCESSFULLY_QUERY, operation, null), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO>  createOperation(@RequestBody @Valid CreateOperationDTO createOperationDTO) {
        Operation operation = this.operationService.createOperation(createOperationDTO);
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.OK.value(), ResponseMessage.SUCCESSFULLY_CREATION, operation, null), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO>  finishOperation(@RequestBody @Valid FinishOperationDTO finishOperationDTO) {
        Operation operation = this.operationService.finishOperation(finishOperationDTO.getOperationId());
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.OK.value(), ResponseMessage.SUCCESSFULLY_UPDATE, operation, null), HttpStatus.OK);
    }

    @GetMapping("/medication/drone/{id}")
    public ResponseEntity<ResponseDTO> getMedOnDrone(@PathVariable("id") Long drone) {
        DroneWithMedsDTO droneWithMedsDTO = this.operationService.getMedicationOnDrone(drone);
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.OK.value(), ResponseMessage.SUCCESSFULLY_QUERY, droneWithMedsDTO, null), HttpStatus.OK);
    }
}

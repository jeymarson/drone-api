package com.musala.drone.controllers;

import com.musala.drone.constants.ResponseMessage;
import com.musala.drone.dtos.ResponseDTO;
import com.musala.drone.models.Drone;
import com.musala.drone.services.DroneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("drone")
public class DroneController {

    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }


    @PostMapping
    public ResponseEntity<ResponseDTO> createDrone(@RequestBody @Valid Drone drone) {
        Drone drone1 = this.droneService.createOrUpdateDrone(drone);
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.CREATED.value(), ResponseMessage.SUCCESSFULLY_CREATION, drone1, null), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateDrone(@RequestBody @Valid Drone drone) {
        Drone drone1 =  this.droneService.createOrUpdateDrone(drone);
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.OK.value(), ResponseMessage.SUCCESSFULLY_UPDATE, drone1, null), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable Long id) {
        Drone drone1 = this.droneService.getDroneById(id);
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.OK.value(), ResponseMessage.SUCCESSFULLY_QUERY, drone1, null), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getAll() {
        List<Drone> drones = this.droneService.getAll();
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.OK.value(), ResponseMessage.SUCCESSFULLY_QUERY, drones, null), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteById(@PathVariable Long id) {
        this.droneService.deleteDroneById(id);
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.OK.value(), ResponseMessage.SUCCESSFULLY_DELETE, null, null), HttpStatus.OK);
    }

    @GetMapping("/available")
    public ResponseEntity<ResponseDTO> getAvailableDrones() {
        List<Drone> drones = this.droneService.getAvailableDrones();
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.OK.value(), ResponseMessage.SUCCESSFULLY_QUERY, drones, null), HttpStatus.OK);
    }
}

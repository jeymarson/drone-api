package com.musala.drone.controllers;

import com.musala.drone.models.Drone;
import com.musala.drone.services.DroneService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("drone")
public class DroneController {

    private final DroneService droneService;

    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }


    @PostMapping
    public Drone createDrone(@RequestBody Drone drone) {
        return this.droneService.createOrUpdateDrone(drone);
    }

    @PutMapping
    public Drone updateDrone(@RequestBody Drone drone) {
        return this.droneService.createOrUpdateDrone(drone);
    }

    @GetMapping("/{id}")
    public Drone getById(@PathVariable Long id) {
        return this.droneService.getDroneById(id);
    }

    @GetMapping
    public List<Drone> getAll() {
        return this.droneService.getAll();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        this.droneService.deleteDroneById(id);
        return ResponseEntity.noContent().build();
    }
}

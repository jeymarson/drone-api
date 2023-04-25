package com.musala.drone.services;

import com.musala.drone.constants.DroneState;
import com.musala.drone.exceptions.ResourceNotFoundException;
import com.musala.drone.models.Drone;
import com.musala.drone.repositories.DroneRepository;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DroneService {

    private final DroneRepository droneRepository;

    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    public Drone createOrUpdateDrone(Drone drone) {
        if (Objects.isNull(drone.getCreatedAt())) {
            drone.setCreatedAt(ZonedDateTime.now().toLocalDateTime());
        }

        return this.droneRepository.save(drone);
    }

    public List<Drone> getAll() {
        return this.droneRepository.findAll();
    }

    public Drone getDroneById(@NotNull Long id) {
        Optional<Drone> drone = this.droneRepository.findById(id);

        if (drone.isPresent()) {
            return drone.get();
        }

        throw new ResourceNotFoundException("Drone");
    }

    public void deleteDroneById(@NotNull Long id) {
        this.droneRepository.deleteById(id);
    }

    public List<Drone> getAvailableDrones() {
        return  this.droneRepository.findAllByStateInAndBatteryCapacityGreaterThanEqual(List.of(DroneState.LOADED, DroneState.IDLE), 25);
    }
}

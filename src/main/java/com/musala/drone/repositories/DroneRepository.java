package com.musala.drone.repositories;

import com.musala.drone.constants.DroneState;
import com.musala.drone.models.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {

    List<Drone> findAllByStateInAndBatteryCapacityGreaterThanEqual(List<DroneState> states, int batteryCapacity);
    List<Drone> findAllByBatteryCapacityLessThan(int battery);
    List<Drone> findAllByState(DroneState state);
}

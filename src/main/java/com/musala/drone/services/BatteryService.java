package com.musala.drone.services;

import com.musala.drone.constants.DroneState;
import com.musala.drone.models.BatteryEvent;
import com.musala.drone.models.Drone;
import com.musala.drone.repositories.BatteryEventRepository;
import com.musala.drone.repositories.DroneRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class BatteryService {

    private final DroneRepository droneRepository;
    private final BatteryEventRepository batteryEventRepository;

    public BatteryService(DroneRepository droneRepository, BatteryEventRepository batteryEventRepository) {
        this.droneRepository = droneRepository;
        this.batteryEventRepository = batteryEventRepository;
    }


    @Scheduled(fixedRate = 60000)
    public void checkAndUpdateLoadingDrones() {
        List<Drone> drones = this.droneRepository.findAllByState(DroneState.LOADING);
        List<BatteryEvent> batteryEvents = new ArrayList<>();

        drones.forEach(d -> {
            d.setBatteryCapacity(100);
            d.setState(DroneState.IDLE);

            BatteryEvent batteryEvent = new BatteryEvent();
            batteryEvent.setDrone(d);
            batteryEvent.setCreatedAt(ZonedDateTime.now().toLocalDateTime());
            batteryEvent.setNewState(DroneState.IDLE.name());
            batteryEvent.setPrevState(DroneState.LOADING.name());

            batteryEvents.add(batteryEvent);
        });

        this.batteryEventRepository.saveAll(batteryEvents);
        this.droneRepository.saveAll(drones);
    }

    @Scheduled(fixedRate = 60000)
    public void checkAndUpdateLowBatteryDrones() {
        List<Drone> drones = this.droneRepository.findAllByBatteryCapacityLessThan(25);
        List<BatteryEvent> batteryEvents = new ArrayList<>();

        drones.stream().filter(d -> !DroneState.LOADING.equals(d.getState())).forEach(d -> {
            d.setState(DroneState.LOADING);

            BatteryEvent batteryEvent = new BatteryEvent();
            batteryEvent.setDrone(d);
            batteryEvent.setCreatedAt(ZonedDateTime.now().toLocalDateTime());
            batteryEvent.setNewState(DroneState.LOADING.name());
            batteryEvent.setPrevState(d.getState().name());

            batteryEvents.add(batteryEvent);
        });

        this.batteryEventRepository.saveAll(batteryEvents);
        this.droneRepository.saveAll(drones);
    }
}

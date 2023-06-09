package com.musala.drone.utils;

import com.musala.drone.constants.DroneModel;
import com.musala.drone.constants.DroneState;
import com.musala.drone.models.Drone;
import com.musala.drone.models.Medication;
import com.musala.drone.models.Operation;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

public class TestUtils {

    public static Drone getMockDrone() {
        Drone drone = new Drone();

        drone.setBatteryCapacity(99);
        drone.setModel(DroneModel.CRUISERWEIGHT);
        drone.setState(DroneState.IDLE);
        drone.setSerialNumber(UUID.randomUUID().toString());
        drone.setWeightLimit(345.0F);
        drone.setCreatedAt(ZonedDateTime.now().toLocalDateTime());

        return drone;
    }

    public static Drone getMockDroneWithBatteryAndState(int battery, DroneState state) {
        Drone drone = getMockDrone();
        drone.setBatteryCapacity(battery);
        drone.setState(state);

        return drone;
    }

    public static List<Drone> getListMockDrone(int count) {
        List<Drone> drones = new ArrayList<>();
        IntStream.range(0, count).forEach(i -> {
            drones.add(getMockDrone());
        });

        return drones;
    }

    public static Medication getMockMedication() {
        Medication medication = new Medication();
        medication.setCode(UUID.randomUUID().toString());
        medication.setImage("https://test_url");
        medication.setWeight(22.3);
        medication.setName("Test medication");
        medication.setCreatedAt(ZonedDateTime.now().toLocalDateTime());

        return  medication;
    }

    public static List<Medication> getListMockMedication(int count) {
        List<Medication> medications = new ArrayList<>();
        IntStream.range(0, count).forEach(i -> {
            medications.add(getMockMedication());
        });

        return medications;
    }

    public static Operation getMockOperation(Drone drone, List<Medication> medications) {
        Operation operation = new Operation();
        operation.setCreatedAt(ZonedDateTime.now().toLocalDateTime());
        operation.setDrone(drone);
        operation.setMedications(medications);

        return  operation;
    }
}

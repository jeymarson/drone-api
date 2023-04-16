package com.musala.drone.utils;

import com.musala.drone.constants.DroneModel;
import com.musala.drone.constants.DroneState;
import com.musala.drone.models.Drone;
import com.musala.drone.models.Medication;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TestUtils {

    public static Drone getMockDrone() {
        Drone drone = new Drone();

        drone.setBatteryCapacity(99);
        drone.setModel(DroneModel.CRUISERWEIGHT);
        drone.setState(DroneState.IDLE);
        drone.setSerialNumber("110202993838387474");
        drone.setWeightLimit(345.0f);
        drone.setCreatedAt(ZonedDateTime.now().toLocalDateTime());

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
        medication.setCode("CPDPOO02990373MKD");
        medication.setImage("https://test_url");
        medication.setWeight(22.3);
        medication.setName("Test medication");
        medication.setCreatedAt(ZonedDateTime.now().toLocalDateTime());

        return  medication;
    }
}

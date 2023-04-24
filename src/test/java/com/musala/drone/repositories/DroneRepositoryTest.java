package com.musala.drone.repositories;

import com.musala.drone.DroneApplication;
import com.musala.drone.constants.DroneState;
import com.musala.drone.models.Drone;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.musala.drone.utils.TestUtils.getMockDrone;
import static com.musala.drone.utils.TestUtils.getMockDroneWithBatteryAndState;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DroneApplication.class)
@Transactional
public class DroneRepositoryTest {

    @Autowired
    private DroneRepository droneRepository;

    @Test
    public void createDroneShouldWorksCorrectly() {
        Drone drone = getMockDrone();

        Drone saved = this.droneRepository.save(drone);

        assertEquals(drone.getSerialNumber(), saved.getSerialNumber());
        assertNotNull(drone.getId());
    }

    @Test
    public void createDroneWithoutModelShouldThrownException() {
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            Drone drone = getMockDrone();
            drone.setModel(null);
            this.droneRepository.save(drone);
        });

        assertNotNull(exception);
    }

    @Test
    public void createTwoDroneWithSameSerialShouldThrownException() {
        Drone droneOne = getMockDrone();
        Drone droneTwo = getMockDrone();
        this.droneRepository.save(droneOne);
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            this.droneRepository.save(droneTwo);
        });

        assertNotNull(exception);
    }

    @Test
    public void getDroneByIdShouldReturnDrone() {
        Drone drone = getMockDrone();

        Drone saved = this.droneRepository.save(drone);

        Optional<Drone> found = this.droneRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(found.get().getSerialNumber(), drone.getSerialNumber());
    }

    @Test
    public void deleteByIdShouldDeleteCorrectly() {
        Drone drone = getMockDrone();

        Drone saved = this.droneRepository.save(drone);

        this.droneRepository.deleteById(saved.getId());

        Optional<Drone> found = this.droneRepository.findById(saved.getId());

        assertFalse(found.isPresent());
    }

    @Test
    public void  getAvailableDrones() {
        this.droneRepository.save(getMockDroneWithBatteryAndState(20, DroneState.DELIVERED));
        this.droneRepository.save(getMockDroneWithBatteryAndState(80, DroneState.LOADED));
        this.droneRepository.save(getMockDroneWithBatteryAndState(90, DroneState.DELIVERED));
        this.droneRepository.save(getMockDroneWithBatteryAndState(20, DroneState.DELIVERED));

        List<Drone> drones = this.droneRepository.findAllByStateInAndBatteryCapacityGreaterThanEqual(Arrays.asList(DroneState.DELIVERED), 25);

        assertEquals(1, drones.size());
    }
}

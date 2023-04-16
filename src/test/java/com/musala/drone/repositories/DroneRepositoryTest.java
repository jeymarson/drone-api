package com.musala.drone.repositories;

import com.musala.drone.DroneApplication;
import com.musala.drone.constants.DroneModel;
import com.musala.drone.constants.DroneState;
import com.musala.drone.models.Drone;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DroneApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DroneRepositoryTest {

    @Autowired
    private DroneRepository droneRepository;

    @Test
    public void createDroneShouldWorksCorrectly() {
        Drone drone = this.getMockDrone();

        Drone saved = this.droneRepository.save(drone);

        assertEquals(drone.getSerialNumber(), saved.getSerialNumber());
        assertNotNull(drone.getId());
    }

    @Test
    public void createDroneWithoutModelShouldThrownException() {
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            Drone drone = this.getMockDrone();
            drone.setModel(null);
            this.droneRepository.save(drone);
        });

        assertNotNull(exception);
    }

    @Test
    public void getDroneByIdShouldReturnDrone() {
        Drone drone = this.getMockDrone();

        Drone saved = this.droneRepository.save(drone);

        Optional<Drone> found = this.droneRepository.findById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(found.get().getSerialNumber(), drone.getSerialNumber());
    }

    @Test
    public void deleteByIdShouldDeleteCorrectly() {
        Drone drone = this.getMockDrone();

        Drone saved = this.droneRepository.save(drone);

        this.droneRepository.deleteById(saved.getId());

        Optional<Drone> found = this.droneRepository.findById(saved.getId());

        assertFalse(found.isPresent());
    }

    private Drone getMockDrone() {
        Drone drone = new Drone();

        drone.setBatteryCapacity(99);
        drone.setModel(DroneModel.CRUISERWEIGHT);
        drone.setState(DroneState.IDLE);
        drone.setSerialNumber("110202993838387474");
        drone.setWeightLimit(345.0f);
        drone.setCreatedAt(ZonedDateTime.now().toLocalDateTime());

        return drone;
    }
}

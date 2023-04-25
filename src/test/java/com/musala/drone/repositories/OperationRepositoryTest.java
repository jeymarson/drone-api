package com.musala.drone.repositories;

import com.musala.drone.DroneApplication;
import com.musala.drone.models.Drone;
import com.musala.drone.models.Medication;
import com.musala.drone.models.Operation;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;
import java.util.Collections;

import static com.musala.drone.utils.TestUtils.getMockDrone;
import static com.musala.drone.utils.TestUtils.getMockMedication;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DroneApplication.class)
@Transactional
public class OperationRepositoryTest {

    @Autowired
    private OperationRepository operationRepository;
    @Autowired
    private DroneRepository droneRepository;
    @Autowired
    private MedicationRepository medicationRepository;

    @Test
    public void createOperationSuccessfully() {
        Drone drone = this.droneRepository.save(getMockDrone());
        Medication medication = this.medicationRepository.save(getMockMedication());

        Operation operation = new Operation();
        operation.setDrone(drone);
        operation.setMedications(Collections.singletonList(medication));
        operation.setCreatedAt(ZonedDateTime.now().toLocalDateTime());

        Operation operationSaved = this.operationRepository.save(operation);

        assertNotNull(operationSaved);
        assertNotNull(operationSaved.getId());
    }
}

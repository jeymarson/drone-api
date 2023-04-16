package com.musala.drone.repositories;

import com.musala.drone.DroneApplication;
import com.musala.drone.models.Medication;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.musala.drone.utils.TestUtils.getMockMedication;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DroneApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MedicationRepositoryTest {

    @Autowired
    private MedicationRepository medicationRepository;

    @Test
    public void createMedicationShouldWorksCorrectly() {
        Medication medication = getMockMedication();

        Medication saved = this.medicationRepository.save(medication);

        assertNotNull(saved.getId());
    }

    @Test
    public void createDroneWithoutNameShouldThrownException() {
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            Medication medication = getMockMedication();
            medication.setName(null);
            this.medicationRepository.save(medication);
        });

        assertNotNull(exception);
    }
}

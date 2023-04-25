package com.musala.drone.services;

import com.musala.drone.exceptions.ResourceNotFoundException;
import com.musala.drone.models.Medication;
import com.musala.drone.repositories.MedicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.musala.drone.utils.TestUtils.getMockMedication;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MedicationServiceTest {

    @Mock
    private MedicationRepository medicationRepository;

    private MedicationService medicationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.medicationService = new MedicationService(this.medicationRepository);
    }

    @Test
    public void testCreateOrUpdateMedication() {
        Medication medication = getMockMedication();

        when(this.medicationRepository.save(any())).thenReturn(medication);

        Medication savedMedication = this.medicationService.createOrUpdateMedication(medication);

        assertEquals(medication.getName(), savedMedication.getName());
        assertEquals(medication.getWeight(), savedMedication.getWeight());
        assertEquals(medication.getCode(), savedMedication.getCode());
        assertEquals(medication.getImage(), savedMedication.getImage());

        verify(this.medicationRepository, times(1)).save(medication);
    }

    @Test
    public void testGetAll() {
        List<Medication> medications = new ArrayList<>();

        Medication medication1 = getMockMedication();
        medication1.setId(1L);
        medication1.setCode("ABC123");

        Medication medication2 = getMockMedication();
        medication2.setId(2L);
        medication2.setCode("DEF456");

        medications.add(medication1);
        medications.add(medication2);

        when(this.medicationRepository.findAll()).thenReturn(medications);

        List<Medication> retrievedMedications = this.medicationService.getAll();

        assertEquals(medications.size(), retrievedMedications.size());
        assertTrue(retrievedMedications.contains(medication1));
        assertTrue(retrievedMedications.contains(medication2));

        verify(this.medicationRepository, times(1)).findAll();
    }

    @Test
    public void testGetMedicationById() {
        Long medicationId = 1L;
        Medication medication = getMockMedication();
        medication.setId(medicationId);

        when(this.medicationRepository.findById(medicationId)).thenReturn(Optional.of(medication));

        Medication retrievedMedication = this.medicationService.getMedicationById(medicationId);

        assertNotNull(retrievedMedication);
        assertEquals(medicationId, retrievedMedication.getId());
        assertEquals(medication.getName(), retrievedMedication.getName());
    }

        @Test
    public void testDeleteMedicationById() {
        Long medicationId = 1L;
        doNothing().when(this.medicationRepository).deleteById(medicationId);

        assertDoesNotThrow(() -> {
            this.medicationService.deleteMedicationById(medicationId);
        });

        verify(this.medicationRepository, times(1)).deleteById(medicationId);
    }

    @Test
    public void testGetMedicationByIdNotFound() {
        Long medicationId = 1L;

        when(this.medicationRepository.findById(medicationId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            this.medicationService.getMedicationById(medicationId);
        });

        verify(this.medicationRepository, times(1)).findById(medicationId);
    }

}

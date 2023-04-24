package com.musala.drone.controllers;

import com.musala.drone.constants.ResponseMessage;
import com.musala.drone.dtos.ResponseDTO;
import com.musala.drone.models.Medication;
import com.musala.drone.services.MedicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static com.musala.drone.utils.TestUtils.getMockMedication;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MedicationControllerTest {
    private MedicationController medicationController;
    
    @Mock
    private MedicationService medicationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        medicationController = new MedicationController(this.medicationService);
    }

    @Test
    public void testCreateMedication() {
        Medication medication = getMockMedication();
        medication.setId(1L);
        when(medicationService.createOrUpdateMedication(any(Medication.class))).thenReturn(medication);

        ResponseDTO responseDTO = medicationController.createMedication(medication).getBody();
        Medication result = (Medication) responseDTO.getData();

        verify(medicationService, times(1)).createOrUpdateMedication(medication);
        assertEquals(medication, result);
    }

    @Test
    @DisplayName("Update drone")
    public void testUpdateDrone() {
        Medication medication = getMockMedication();
        medication.setId(1L);
        when(medicationService.createOrUpdateMedication(any(Medication.class))).thenReturn(medication);

        ResponseDTO responseDTO = medicationController.updateMedication(medication).getBody();
        Medication result = (Medication) responseDTO.getData();

        verify(medicationService, times(1)).createOrUpdateMedication(medication);
        assertEquals(medication, result);
    }

    @Test
    @DisplayName("Get drone by id")
    public void testGetDroneById() {
        Medication medication = getMockMedication();
        medication.setId(1L);
        when(medicationService.getMedicationById(1L)).thenReturn(medication);

        ResponseDTO responseDTO = medicationController.getMedicationById(1L).getBody();
        Medication result = (Medication) responseDTO.getData();

        verify(medicationService, times(1)).getMedicationById(1L);
        assertEquals(medication, result);
    }

    @Test
    public void testGetAllMedications() {
        Medication medication1 = getMockMedication();
        medication1.setId(1L);
        Medication medication2 = getMockMedication();
        medication2.setId(2L);
        List<Medication> droneList = Arrays.asList(medication1, medication2);
        when(medicationService.getAll()).thenReturn(droneList);

        ResponseDTO responseDTO = medicationController.getAllMedications().getBody();
        List<Medication> result = (List<Medication>) responseDTO.getData();

        verify(medicationService, times(1)).getAll();
        assertEquals(droneList, result);
    }

    @Test
    public void testDeleteMedicationById() {
        doNothing().when(medicationService).deleteMedicationById(1L);

        ResponseDTO responseDTO = medicationController.deleteMedication(1L).getBody();

        verify(medicationService, times(1)).deleteMedicationById(1L);
        assertEquals(ResponseMessage.SUCCESSFULLY_DELETE, responseDTO.getMessage());
    }
}

package com.musala.drone.services;

import com.musala.drone.constants.DroneState;
import com.musala.drone.dtos.CreateOperationDTO;
import com.musala.drone.exceptions.ExceptionMessages;
import com.musala.drone.exceptions.OperationNotAllowedException;
import com.musala.drone.exceptions.ResourceNotFoundException;
import com.musala.drone.models.Drone;
import com.musala.drone.models.Operation;
import com.musala.drone.repositories.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;

import static com.musala.drone.utils.TestUtils.getMockDrone;
import static com.musala.drone.utils.TestUtils.getMockMedication;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OperationServiceTest {

    @Mock
    private DroneService droneService;
    @Mock
    private MedicationService medicationService;
    @Mock
    private OperationRepository operationRepository;

    private OperationService operationService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.operationService = new OperationService(this.operationRepository, this.droneService, this.medicationService);
    }

    @Test
    public void createOperationWithoutDroneReturnException() {
        CreateOperationDTO createOperationDTO = new CreateOperationDTO();
        createOperationDTO.setDroneId(1l);

        when(this.droneService.getDroneById(Mockito.eq(1l))).thenThrow(new ResourceNotFoundException("Drone"));

        ResourceNotFoundException exception =  assertThrows(ResourceNotFoundException.class, () -> {
            this.operationService.createOperation(createOperationDTO);
        });

        verify(this.droneService, times(1)).getDroneById(Mockito.eq(1l));
        assertEquals(String.format(ExceptionMessages.NOT_FOUND_MESSAGE, "Drone"), exception.getMessage());
    }

    @Test
    public void createOperationWithoutMedicationReturnException() {
        CreateOperationDTO createOperationDTO = new CreateOperationDTO();
        createOperationDTO.setDroneId(1l);
        createOperationDTO.setMedicationIds(Collections.singletonList(1l));

        when(this.droneService.getDroneById(Mockito.eq(1l))).thenReturn(getMockDrone());
        when(this.medicationService.getMedicationById(Mockito.eq(1l))).thenThrow(new ResourceNotFoundException("Medication"));

        ResourceNotFoundException exception =  assertThrows(ResourceNotFoundException.class, () -> {
            this.operationService.createOperation(createOperationDTO);
        });

        verify(this.droneService, times(1)).getDroneById(Mockito.eq(1l));
        verify(this.medicationService, times(1)).getMedicationById(Mockito.eq(1l));

        assertEquals(String.format(ExceptionMessages.NOT_FOUND_MESSAGE, "Medication"), exception.getMessage());
    }

    @Test
    public void createOperationMedicationIsTooMuchReturnException() {
        CreateOperationDTO createOperationDTO = new CreateOperationDTO();
        createOperationDTO.setDroneId(1l);
        createOperationDTO.setMedicationIds(Arrays.asList(1l, 2l, 3l, 4l, 5l, 6l));

        Drone drone = getMockDrone();
        drone.setWeightLimit(100f);

        when(this.droneService.getDroneById(Mockito.eq(1l))).thenReturn(drone);
        when(this.medicationService.getMedicationById(Mockito.any())).thenReturn(getMockMedication());

        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class, () -> {
            this.operationService.createOperation(createOperationDTO);
        });

        verify(this.droneService, times(1)).getDroneById(Mockito.eq(1l));
        verify(this.medicationService, times(6)).getMedicationById(Mockito.any());

        assertNotNull(exception);
        assertEquals("The weight of the medicines exceeds the limit weight of the drone.", exception.getMessage());
    }

    @Test
    public void createOperationWhenDroneHaveNotBatteryReturnException() {
        CreateOperationDTO createOperationDTO = new CreateOperationDTO();
        createOperationDTO.setDroneId(1l);
        createOperationDTO.setMedicationIds(Arrays.asList(1l, 2l));

        Drone drone = getMockDrone();
        drone.setBatteryCapacity(12);

        when(this.droneService.getDroneById(Mockito.eq(1l))).thenReturn(drone);
        when(this.medicationService.getMedicationById(Mockito.any())).thenReturn(getMockMedication());

        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class, () -> {
            this.operationService.createOperation(createOperationDTO);
        });

        verify(this.droneService, times(1)).getDroneById(Mockito.eq(1l));
        verify(this.medicationService, times(2)).getMedicationById(Mockito.any());

        assertNotNull(exception);
        assertEquals("The drone does not have enough battery to perform the operation.", exception.getMessage());
    }

    @Test
    public void createOperationWhenDroneIsNotAvailableReturnException() {
        CreateOperationDTO createOperationDTO = new CreateOperationDTO();
        createOperationDTO.setDroneId(1l);
        createOperationDTO.setMedicationIds(Arrays.asList(1l, 2l));

        Drone drone = getMockDrone();
        drone.setState(DroneState.LOADING);

        when(this.droneService.getDroneById(Mockito.eq(1l))).thenReturn(drone);
        when(this.medicationService.getMedicationById(Mockito.any())).thenReturn(getMockMedication());

        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class, () -> {
            this.operationService.createOperation(createOperationDTO);
        });

        verify(this.droneService, times(1)).getDroneById(Mockito.eq(1l));
        verify(this.medicationService, times(2)).getMedicationById(Mockito.any());

        assertNotNull(exception);
        assertEquals("The drone does not allowed.", exception.getMessage());
    }

    @Test
    public void createOperationShouldWorksSuccessfully() {
        CreateOperationDTO createOperationDTO = new CreateOperationDTO();
        createOperationDTO.setDroneId(1l);
        createOperationDTO.setMedicationIds(Arrays.asList(1l, 2l));

        Drone drone = getMockDrone();
        drone.setId(1l);

        when(this.droneService.getDroneById(Mockito.eq(1l))).thenReturn(drone);
        when(this.medicationService.getMedicationById(Mockito.any())).thenReturn(getMockMedication());
        when(this.droneService.createOrUpdateDrone(Mockito.any(Drone.class))).thenReturn(drone);
        when(this.operationRepository.save(Mockito.any(Operation.class))).thenReturn(new Operation());

        Operation operation = this.operationService.createOperation(createOperationDTO);

        verify(this.droneService, times(1)).getDroneById(Mockito.eq(1l));
        verify(this.droneService, times(1)).createOrUpdateDrone(Mockito.any());
        verify(this.medicationService, times(2)).getMedicationById(Mockito.any());

        assertNotNull(operation);
    }
}

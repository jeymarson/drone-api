package com.musala.drone.services;

import com.musala.drone.constants.DroneState;
import com.musala.drone.dtos.CreateOperationDTO;
import com.musala.drone.dtos.DroneWithMedsDTO;
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

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.musala.drone.utils.TestUtils.*;
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
        MockitoAnnotations.openMocks(this);
        this.operationService = new OperationService(this.operationRepository, this.droneService, this.medicationService);
    }

    @Test
    public void createOperationWithoutDroneReturnException() {
        CreateOperationDTO createOperationDTO = new CreateOperationDTO();
        createOperationDTO.setDroneId(1L);

        when(this.droneService.getDroneById(Mockito.eq(1L))).thenThrow(new ResourceNotFoundException("Drone"));

        ResourceNotFoundException exception =  assertThrows(ResourceNotFoundException.class, () -> {
            this.operationService.createOperation(createOperationDTO);
        });

        verify(this.droneService, times(1)).getDroneById(Mockito.eq(1L));
        assertEquals(String.format(ExceptionMessages.NOT_FOUND_MESSAGE, "Drone"), exception.getMessage());
    }

    @Test
    public void createOperationWithoutMedicationReturnException() {
        CreateOperationDTO createOperationDTO = new CreateOperationDTO();
        createOperationDTO.setDroneId(1L);
        createOperationDTO.setMedicationIds(Collections.singletonList(1L));

        when(this.droneService.getDroneById(Mockito.eq(1L))).thenReturn(getMockDrone());
        when(this.medicationService.getMedicationById(Mockito.eq(1L))).thenThrow(new ResourceNotFoundException("Medication"));

        ResourceNotFoundException exception =  assertThrows(ResourceNotFoundException.class, () -> {
            this.operationService.createOperation(createOperationDTO);
        });

        verify(this.droneService, times(1)).getDroneById(Mockito.eq(1L));
        verify(this.medicationService, times(1)).getMedicationById(Mockito.eq(1L));

        assertEquals(String.format(ExceptionMessages.NOT_FOUND_MESSAGE, "Medication"), exception.getMessage());
    }

    @Test
    public void createOperationMedicationIsTooMuchReturnException() {
        CreateOperationDTO createOperationDTO = new CreateOperationDTO();
        createOperationDTO.setDroneId(1L);
        createOperationDTO.setMedicationIds(List.of(1L, 2L, 3L, 4L, 5L, 6L));

        Drone drone = getMockDrone();
        drone.setWeightLimit(100f);

        when(this.droneService.getDroneById(Mockito.eq(1L))).thenReturn(drone);
        when(this.medicationService.getMedicationById(Mockito.any())).thenReturn(getMockMedication());

        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class, () -> {
            this.operationService.createOperation(createOperationDTO);
        });

        verify(this.droneService, times(1)).getDroneById(Mockito.eq(1L));
        verify(this.medicationService, times(6)).getMedicationById(Mockito.any());

        assertNotNull(exception);
        assertEquals("The weight of the medicines exceeds the limit weight of the drone.", exception.getMessage());
    }

    @Test
    public void createOperationWhenDroneHaveNotBatteryReturnException() {
        CreateOperationDTO createOperationDTO = new CreateOperationDTO();
        createOperationDTO.setDroneId(1L);
        createOperationDTO.setMedicationIds(List.of(1L, 2L));

        Drone drone = getMockDrone();
        drone.setBatteryCapacity(12);

        when(this.droneService.getDroneById(Mockito.eq(1L))).thenReturn(drone);
        when(this.medicationService.getMedicationById(Mockito.any())).thenReturn(getMockMedication());

        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class, () -> {
            this.operationService.createOperation(createOperationDTO);
        });

        verify(this.droneService, times(1)).getDroneById(Mockito.eq(1L));
        verify(this.medicationService, times(2)).getMedicationById(Mockito.any());

        assertNotNull(exception);
        assertEquals("The drone does not have enough battery to perform the operation.", exception.getMessage());
    }

    @Test
    public void createOperationWhenDroneIsNotAvailableReturnException() {
        CreateOperationDTO createOperationDTO = new CreateOperationDTO();
        createOperationDTO.setDroneId(1L);
        createOperationDTO.setMedicationIds(List.of(1L, 2L));

        Drone drone = getMockDrone();
        drone.setState(DroneState.LOADING);

        when(this.droneService.getDroneById(Mockito.eq(1L))).thenReturn(drone);
        when(this.medicationService.getMedicationById(Mockito.any())).thenReturn(getMockMedication());

        OperationNotAllowedException exception = assertThrows(OperationNotAllowedException.class, () -> {
            this.operationService.createOperation(createOperationDTO);
        });

        verify(this.droneService, times(1)).getDroneById(Mockito.eq(1L));
        verify(this.medicationService, times(2)).getMedicationById(Mockito.any());

        assertNotNull(exception);
        assertEquals("The drone does not allowed.", exception.getMessage());
    }

    @Test
    public void createOperationShouldWorksSuccessfully() {
        CreateOperationDTO createOperationDTO = new CreateOperationDTO();
        createOperationDTO.setDroneId(1L);
        createOperationDTO.setMedicationIds(List.of(1L, 2L));

        Drone drone = getMockDrone();
        drone.setId(1L);

        when(this.droneService.getDroneById(Mockito.eq(1L))).thenReturn(drone);
        when(this.medicationService.getMedicationById(Mockito.any())).thenReturn(getMockMedication());
        when(this.droneService.createOrUpdateDrone(Mockito.any(Drone.class))).thenReturn(drone);
        when(this.operationRepository.save(Mockito.any(Operation.class))).thenReturn(new Operation());

        Operation operation = this.operationService.createOperation(createOperationDTO);

        verify(this.droneService, times(1)).getDroneById(Mockito.eq(1L));
        verify(this.droneService, times(1)).createOrUpdateDrone(Mockito.any());
        verify(this.medicationService, times(2)).getMedicationById(Mockito.any());

        assertNotNull(operation);
    }

    @Test
    public void getOperationByIdShouldWorksCorrectly() {
        when(this.operationRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(new Operation()));

        Operation operation = this.operationService.getOperationById(1L);

        verify(this.operationRepository, times(1)).findById(Mockito.eq(1L));
        assertNotNull(operation);
    }

    @Test
    public void getOperationByIdShouldReturnsException() {
        when(this.operationRepository.findById(Mockito.eq(1L))).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            this.operationService.getOperationById(1L);
        });

        verify(this.operationRepository, times(1)).findById(Mockito.eq(1L));
        assertNotNull(exception);
    }

    @Test
    public void getOpenOperationsByIdShouldWorksCorrectly() {
        when(this.operationRepository.findAllByTerminatedAtIsNull()).thenReturn(Collections.singletonList(new Operation()));

        List<Operation> operations = this.operationService.getOpenOperations();

        verify(this.operationRepository, times(1)).findAllByTerminatedAtIsNull();
        assertNotNull(operations);
        assertEquals(1, operations.size());
    }

    @Test
    public void finishOperationShouldWorksCorrectly() {
        Drone drone = getMockDrone();
        drone.setId(1L);

        Operation operation = getMockOperation(drone, Collections.emptyList());

        when(this.operationRepository.findById(Mockito.eq(1L))).thenReturn(Optional.of(operation));
        when(this.droneService.createOrUpdateDrone(Mockito.any(Drone.class))).thenReturn(drone);
        when(this.operationRepository.save(Mockito.any(Operation.class))).thenReturn(operation);

        Operation operationSaved = this.operationService.finishOperation(1L);

        verify(this.operationRepository, times(1)).findById(Mockito.eq(1L));
        verify(this.droneService, times(1)).createOrUpdateDrone(Mockito.any());
        verify(this.operationRepository, times(1)).save(Mockito.any());

        assertNotNull(operationSaved);
    }

    @Test
    public void finishOperationThrownException() {
        when(this.operationRepository.findById(Mockito.eq(1L))).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            this.operationService.finishOperation(1L);
        });

        verify(this.operationRepository, times(1)).findById(Mockito.eq(1L));

        assertNotNull(exception);
    }

    @Test
    public void getMedicationsOnDroneShouldWorksCorrectly() {
        Drone drone = getMockDrone();

        when(this.droneService.getDroneById(Mockito.eq(1L))).thenReturn(drone);
        when(this.operationRepository.findFirstByDroneAndTerminatedAtIsNullOrderByTerminatedAtDesc(Mockito.eq(drone)))
                .thenReturn(Optional.of(getMockOperation(drone, Collections.emptyList())));

        DroneWithMedsDTO droneWithMedsDTO = this.operationService.getMedicationOnDrone(1L);

        verify(this.droneService, times(1)).getDroneById(Mockito.eq(1L));
        verify(this.operationRepository, times(1)).findFirstByDroneAndTerminatedAtIsNullOrderByTerminatedAtDesc(Mockito.eq(drone));

        assertNotNull(droneWithMedsDTO);
        assertEquals(0, droneWithMedsDTO.getMedications().size());
    }

    @Test
    public void getMedicationsOnDroneShouldThrownException() {
        Drone drone = getMockDrone();

        when(this.droneService.getDroneById(Mockito.eq(1L))).thenReturn(drone);
        when(this.operationRepository.findFirstByDroneAndTerminatedAtIsNullOrderByTerminatedAtDesc(Mockito.eq(drone)))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            this.operationService.getMedicationOnDrone(1L);
        });

        verify(this.droneService, times(1)).getDroneById(Mockito.eq(1L));
        verify(this.operationRepository, times(1)).findFirstByDroneAndTerminatedAtIsNullOrderByTerminatedAtDesc(Mockito.eq(drone));

        assertNotNull(exception);
    }
}

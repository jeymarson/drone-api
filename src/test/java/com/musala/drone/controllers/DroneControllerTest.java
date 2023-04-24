package com.musala.drone.controllers;

import com.musala.drone.constants.ResponseMessage;
import com.musala.drone.dtos.ResponseDTO;
import com.musala.drone.models.Drone;
import com.musala.drone.services.DroneService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static com.musala.drone.utils.TestUtils.getMockDrone;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DroneControllerTest {

    private DroneController droneController;

    @Mock
    private DroneService droneService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        droneController = new DroneController(droneService);
    }

    @Test
    @DisplayName("Create drone")
    public void testCreateDrone() {
        Drone drone = getMockDrone();
        drone.setId(1L);
        when(droneService.createOrUpdateDrone(any(Drone.class))).thenReturn(drone);

        ResponseDTO responseDTO = droneController.createDrone(drone).getBody();
        Drone result = (Drone) responseDTO.getData();

        verify(droneService, times(1)).createOrUpdateDrone(drone);
        assertEquals(drone, result);
    }

    @Test
    @DisplayName("Update drone")
    public void testUpdateDrone() {
        Drone drone = getMockDrone();
        drone.setId(1L);
        when(droneService.createOrUpdateDrone(any(Drone.class))).thenReturn(drone);

        ResponseDTO responseDTO = droneController.updateDrone(drone).getBody();
        Drone result = (Drone) responseDTO.getData();

        verify(droneService, times(1)).createOrUpdateDrone(drone);
        assertEquals(drone, result);
    }

    @Test
    @DisplayName("Get drone by id")
    public void testGetDroneById() {
        Drone drone = getMockDrone();
        drone.setId(1L);
        when(droneService.getDroneById(1L)).thenReturn(drone);

        ResponseDTO responseDTO = droneController.getById(1L).getBody();
        Drone result = (Drone) responseDTO.getData();

        verify(droneService, times(1)).getDroneById(1L);
        assertEquals(drone, result);
    }

    @Test
    @DisplayName("Get all drones")
    public void testGetAllDrones() {
        Drone drone1 = getMockDrone();
        drone1.setId(1L);
        Drone drone2 = getMockDrone();
        drone2.setId(2L);
        List<Drone> droneList = Arrays.asList(drone1, drone2);
        when(droneService.getAll()).thenReturn(droneList);

        ResponseDTO responseDTO = droneController.getAll().getBody();
        List<Drone> result = (List<Drone>) responseDTO.getData();

        verify(droneService, times(1)).getAll();
        assertEquals(droneList, result);
    }

    @Test
    @DisplayName("Delete drone by id")
    public void testDeleteDroneById() {
        doNothing().when(droneService).deleteDroneById(1L);

        ResponseDTO responseDTO = droneController.deleteById(1L).getBody();

        verify(droneService, times(1)).deleteDroneById(1L);
        assertEquals(ResponseMessage.SUCCESSFULLY_DELETE, responseDTO.getMessage());
    }
}

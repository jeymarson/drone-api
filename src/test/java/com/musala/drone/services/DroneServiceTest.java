package com.musala.drone.services;

import com.musala.drone.DroneApplication;
import com.musala.drone.exceptions.ResourceNotFoundException;
import com.musala.drone.models.Drone;
import com.musala.drone.repositories.DroneRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static com.musala.drone.utils.TestUtils.getListMockDrone;
import static com.musala.drone.utils.TestUtils.getMockDrone;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DroneApplication.class)
public class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;

    private DroneService droneService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        this.droneService = new DroneService(this.droneRepository);
    }

    @Test
    public void whenCreateDroneShouldWorkCorrectly() {
        Drone drone = getMockDrone();
        drone.setId(1l);
        when(this.droneRepository.save(Mockito.any(Drone.class))).thenReturn(drone);

        Drone saved = this.droneService.createOrUpdateDrone(new Drone());

        verify(this.droneRepository,times(1)).save(Mockito.any(Drone.class));
        assertEquals(drone.getId(), saved.getId());
        assertEquals(drone, saved);
    }

    @Test
    public void whenGetAllDroneShouldWorksCorrectly() {
        List<Drone> list = getListMockDrone(10);
        when(this.droneRepository.findAll()).thenReturn(list);

        List<Drone> droneList = this.droneService.getAll();

        verify(this.droneRepository, times(1)).findAll();
        assertEquals(10, droneList.size());
    }

    @Test
    public void whenGetByIdShouldWorksCorrectly() {
        Drone drone = getMockDrone();
        drone.setId(1l);

        when(this.droneRepository.findById(Mockito.eq(1l))).thenReturn(Optional.of(drone));

        Drone found = this.droneService.getDroneById(1l);

        verify(this.droneRepository, times(1)).findById(Mockito.eq(1l));
        assertNotNull(found);
        assertEquals(1l, found.getId());
    }

    @Test
    public void whenGetByIdIsEmptyShouldThrownException() {
        when(this.droneRepository.findById(Mockito.eq(1l))).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            this.droneService.getDroneById(1l);
        });

        verify(this.droneRepository, times(1)).findById(Mockito.eq(1l));
        assertNotNull(exception);
        assertEquals("Drone not found", exception.getMessage());
    }

    @Test
    public void whenDeleteDroneByIdShouldWorksCorrectly() {
        this.droneService.deleteDroneById(1l);
        verify(this.droneRepository, times(1)).deleteById(Mockito.eq(1l));
    }

    @Test
    public void getAvailableDroneShouldWorksCorrectly() {
        List<Drone> list = getListMockDrone(10);
        when(this.droneRepository.findAllByStateInAndBatteryCapacityGreaterThanEqual(Mockito.any(), Mockito.eq(25))).thenReturn(list);

        List<Drone> droneList = this.droneService.getAvailableDrones();

        verify(this.droneRepository, times(1)).findAllByStateInAndBatteryCapacityGreaterThanEqual(Mockito.any(), Mockito.eq(25));
        assertEquals(10, droneList.size());
    }
}

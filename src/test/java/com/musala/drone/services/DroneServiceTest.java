package com.musala.drone.services;

import com.musala.drone.DroneApplication;
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

import static com.musala.drone.utils.TestUtils.getMockDrone;
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
    }
}

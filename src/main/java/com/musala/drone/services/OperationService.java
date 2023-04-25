package com.musala.drone.services;

import com.musala.drone.constants.DroneState;
import com.musala.drone.dtos.CreateOperationDTO;
import com.musala.drone.dtos.DroneWithMedsDTO;
import com.musala.drone.exceptions.OperationNotAllowedException;
import com.musala.drone.exceptions.ResourceNotFoundException;
import com.musala.drone.models.Drone;
import com.musala.drone.models.Medication;
import com.musala.drone.models.Operation;
import com.musala.drone.repositories.OperationRepository;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OperationService {

    private final OperationRepository operationRepository;
    private final DroneService droneService;
    private final MedicationService medicationService;

    public OperationService(OperationRepository operationRepository, DroneService droneService, MedicationService medicationService) {
        this.operationRepository = operationRepository;

        this.droneService = droneService;
        this.medicationService = medicationService;
    }

    public Operation getOperationById(Long id) {
        Optional<Operation> operation = this.operationRepository.findById(id);

        if (operation.isEmpty()) {
            throw  new ResourceNotFoundException(Operation.class.toString());
        }

        return operation.get();
    }

    public List<Operation> getOpenOperations() {
        return this.operationRepository.findAllByTerminatedAtIsNull();
    }

    public Operation createOperation(CreateOperationDTO createOperationDTO) {
        Drone drone =  this.droneService.getDroneById(createOperationDTO.getDroneId());
        List<Medication> medications = new ArrayList<>();
        createOperationDTO.getMedicationIds().forEach(id -> {
            medications.add(this.medicationService.getMedicationById(id));
        });

        validateOperation(drone, medications);

        Operation operation = new Operation();
        operation.setDrone(drone);
        operation.setMedications(medications);
        operation.setCreatedAt(ZonedDateTime.now().toLocalDateTime());

        Operation operationSaved = this.operationRepository.save(operation);

        drone.setState(DroneState.DELIVERING);

        this.droneService.createOrUpdateDrone(drone);

        return  operationSaved;
    }

    public Operation finishOperation(Long operationId) {
        Operation operation = this.getOperationById(operationId);

        operation.setTerminatedAt(ZonedDateTime.now().toLocalDateTime());

        Operation operationSaved =  this.operationRepository.save(operation);

        Drone drone = operation.getDrone();
        int battery = drone.getBatteryCapacity() - 15;

        drone.setState(battery < 25 ? DroneState.LOADING : DroneState.IDLE);
        drone.setBatteryCapacity(battery);

        this.droneService.createOrUpdateDrone(drone);

        return  operationSaved;
    }

    public void validateOperation(Drone drone, List<Medication> medications) {
        if (drone.getBatteryCapacity() < 25) {
            throw new OperationNotAllowedException("The drone does not have enough battery to perform the operation.");
        }

        if (List.of(DroneState.LOADING, DroneState.DELIVERING, DroneState.RETURNING).contains(drone.getState())) {
            throw new OperationNotAllowedException("The drone does not allowed.");
        }

        Double weight = medications.stream().map(m -> m.getWeight()).reduce((w, medication )-> w + medication).get();

        if (weight > drone.getWeightLimit()) {
            throw new OperationNotAllowedException("The weight of the medicines exceeds the limit weight of the drone.");
        }
    }

    public DroneWithMedsDTO getMedicationOnDrone(Long droneId) {
        Drone drone = this.droneService.getDroneById(droneId);
        Optional<Operation> operation = this.operationRepository.findFirstByDroneAndTerminatedAtIsNullOrderByTerminatedAtDesc(drone);

        if (operation.isEmpty()) {
            throw new ResourceNotFoundException("Operation");
        }

        DroneWithMedsDTO droneWithMedsDTO = new DroneWithMedsDTO();
        droneWithMedsDTO.setOperationId(operation.get().getId());
        droneWithMedsDTO.setMedications(operation.get().getMedications());
        droneWithMedsDTO.setDrone(drone);

        return droneWithMedsDTO;
    }

}

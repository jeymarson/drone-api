package com.musala.drone.dtos;

import com.musala.drone.models.Drone;
import com.musala.drone.models.Medication;

import java.util.List;

public class DroneWithMedsDTO {

    Drone drone;
    Long operationId;
    List<Medication> medications;

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void setMedications(List<Medication> medications) {
        this.medications = medications;
    }
}

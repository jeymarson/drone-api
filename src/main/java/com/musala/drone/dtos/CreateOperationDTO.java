package com.musala.drone.dtos;

import javax.validation.constraints.NotNull;
import java.util.List;

public class CreateOperationDTO {

    @NotNull
    private Long droneId;
    @NotNull
    private List<Long> medicationIds;

    public Long getDroneId() {
        return droneId;
    }

    public void setDroneId(Long droneId) {
        this.droneId = droneId;
    }

    public List<Long> getMedicationIds() {
        return medicationIds;
    }

    public void setMedicationIds(List<Long> medicationIds) {
        this.medicationIds = medicationIds;
    }
}

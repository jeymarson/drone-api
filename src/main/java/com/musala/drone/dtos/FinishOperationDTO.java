package com.musala.drone.dtos;

import javax.validation.constraints.NotNull;

public class FinishOperationDTO {
    @NotNull
    private Long operationId;

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }
}

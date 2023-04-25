package com.musala.drone.repositories;

import com.musala.drone.models.Drone;
import com.musala.drone.models.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

    List<Operation> findAllByTerminatedAtIsNull();
    Optional<Operation> findFirstByDroneAndTerminatedAtIsNullOrderByTerminatedAtDesc(Drone drone);
}

package com.musala.drone.repositories;

import com.musala.drone.models.BatteryEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatteryEventRepository extends JpaRepository<BatteryEvent, Long> {
}

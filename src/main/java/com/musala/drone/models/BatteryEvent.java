package com.musala.drone.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "battery_event")
public class BatteryEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "drone_id", nullable = false)
    private Drone drone;

    @Column(name = "prev_state", nullable = false)
    private String prevState;

    @Column(name = "new_state", nullable = false)
    private String newState;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public BatteryEvent() {
    }

    public BatteryEvent(Long id, Drone drone, String prevState, String newState, LocalDateTime createdAt) {
        this.id = id;
        this.drone = drone;
        this.prevState = prevState;
        this.newState = newState;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public String getPrevState() {
        return prevState;
    }

    public void setPrevState(String prevState) {
        this.prevState = prevState;
    }

    public String getNewState() {
        return newState;
    }

    public void setNewState(String newState) {
        this.newState = newState;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

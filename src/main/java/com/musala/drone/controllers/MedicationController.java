package com.musala.drone.controllers;

import com.musala.drone.models.Medication;
import com.musala.drone.services.MedicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@RestController
@RequestMapping("medication")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public ResponseEntity<List<Medication>> getAllMedications() {
        List<Medication> medications = medicationService.getAll();
        return new ResponseEntity<>(medications, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medication> getMedicationById(@PathVariable("id") Long id) {
        Medication medication = medicationService.getMedicationById(id);
        return new ResponseEntity<>(medication, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Medication> createMedication(@RequestBody @Valid Medication medication) {
        Medication savedMedication = medicationService.createOrUpdateMedication(medication);
        return new ResponseEntity<>(savedMedication, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medication> updateMedication(@PathVariable("id") Long id, @RequestBody @Valid Medication medication) {
        Medication savedMedication = medicationService.createOrUpdateMedication(medication);
        return new ResponseEntity<>(savedMedication, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable("id") Long id) {
        medicationService.deleteMedicationById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

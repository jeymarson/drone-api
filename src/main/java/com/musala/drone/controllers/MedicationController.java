package com.musala.drone.controllers;

import com.musala.drone.constants.ResponseMessage;
import com.musala.drone.dtos.ResponseDTO;
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
    public ResponseEntity<ResponseDTO> getAllMedications() {
        List<Medication> medications = medicationService.getAll();
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.OK.value(), ResponseMessage.SUCCESSFULLY_QUERY, medications, null), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getMedicationById(@PathVariable("id") Long id) {
        Medication medication = medicationService.getMedicationById(id);
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.OK.value(), ResponseMessage.SUCCESSFULLY_QUERY, medication, null), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> createMedication(@RequestBody @Valid Medication medication) {
        Medication savedMedication = medicationService.createOrUpdateMedication(medication);
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.CREATED.value(), ResponseMessage.SUCCESSFULLY_CREATION, savedMedication, null), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateMedication(@RequestBody @Valid Medication medication) {
        Medication savedMedication = medicationService.createOrUpdateMedication(medication);
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.OK.value(), ResponseMessage.SUCCESSFULLY_UPDATE, savedMedication, null), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDTO> deleteMedication(@PathVariable("id") Long id) {
        medicationService.deleteMedicationById(id);
        return new ResponseEntity<>(new ResponseDTO(HttpStatus.OK.value(), ResponseMessage.SUCCESSFULLY_DELETE, null, null), HttpStatus.OK);
    }
}

package com.musala.drone.services;

import com.musala.drone.exceptions.ResourceNotFoundException;
import com.musala.drone.models.Medication;
import com.musala.drone.repositories.MedicationRepository;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Service
public class MedicationService {
    private final MedicationRepository medicationRepository;


    public MedicationService(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    public Medication createOrUpdateMedication(Medication medication) {
        return this.medicationRepository.save(medication);
    }

    public List<Medication> getAll() {
        return this.medicationRepository.findAll();
    }

    public Medication getMedicationById(@NotNull Long id) {
        Optional<Medication> medication = this.medicationRepository.findById(id);

        if (medication.isPresent()) {
            return medication.get();
        }

        throw new ResourceNotFoundException("Medication");
    }

    public void deleteMedicationById(@NotNull Long id) {
        this.medicationRepository.deleteById(id);
    }
}

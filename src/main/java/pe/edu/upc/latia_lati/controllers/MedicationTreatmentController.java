package pe.edu.upc.latia_lati.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.latia_lati.entities.MedicationTreatment;
import pe.edu.upc.latia_lati.repositories.IMedicationTreatmentRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medicationtreatments")
public class MedicationTreatmentController {
    private final IMedicationTreatmentRepository repository;

    public MedicationTreatmentController(IMedicationTreatmentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<MedicationTreatment>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<MedicationTreatment> registrar(@RequestBody MedicationTreatment treatment) {
        MedicationTreatment saved = repository.save(treatment);
        return ResponseEntity.created(URI.create("/api/medicationtreatments/" + saved.getId())).body(saved);
    }
}

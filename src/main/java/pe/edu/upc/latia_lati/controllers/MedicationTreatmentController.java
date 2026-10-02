package pe.edu.upc.latia_lati.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.latia_lati.entities.MedicationTreatment;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
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
        treatment.setId(null);
        MedicationTreatment saved = repository.save(treatment);
        return ResponseEntity.created(URI.create("/api/medicationtreatments/" + saved.getId())).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationTreatment> buscarPorId(@PathVariable Long id) {
        MedicationTreatment treatment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un tratamiento de medicamento con el id: " + id));
        return ResponseEntity.ok(treatment);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationTreatment> actualizar(
            @PathVariable Long id,
            @RequestBody MedicationTreatment request) {
        MedicationTreatment treatment = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un tratamiento de medicamento con el id: " + id));

        treatment.setClinicalRecord(request.getClinicalRecord());
        treatment.setMedication(request.getMedication());
        treatment.setDose(request.getDose());
        treatment.setAdministrationRoute(request.getAdministrationRoute());
        treatment.setStartDate(request.getStartDate());
        treatment.setEndDate(request.getEndDate());
        return ResponseEntity.ok(repository.save(treatment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "No existe un tratamiento de medicamento con el id: " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

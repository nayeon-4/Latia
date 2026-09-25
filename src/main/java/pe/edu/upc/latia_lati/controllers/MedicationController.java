package pe.edu.upc.latia_lati.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.latia_lati.entities.Medication;
import pe.edu.upc.latia_lati.repositories.IMedicationRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {
    private final IMedicationRepository repository;

    public MedicationController(IMedicationRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<Medication>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<Medication> registrar(@RequestBody Medication medication) {
        Medication saved = repository.save(medication);
        return ResponseEntity.created(URI.create("/api/medications/" + saved.getId())).body(saved);
    }
}

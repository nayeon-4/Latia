package pe.edu.upc.latia_lati.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.latia_lati.entities.MedicationSchedule;
import pe.edu.upc.latia_lati.repositories.IMedicationScheduleRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medicationschedules")
public class MedicationScheduleController {
    private final IMedicationScheduleRepository repository;

    public MedicationScheduleController(IMedicationScheduleRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<MedicationSchedule>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<MedicationSchedule> registrar(@RequestBody MedicationSchedule schedule) {
        MedicationSchedule saved = repository.save(schedule);
        return ResponseEntity.created(URI.create("/api/medicationschedules/" + saved.getId())).body(saved);
    }
}

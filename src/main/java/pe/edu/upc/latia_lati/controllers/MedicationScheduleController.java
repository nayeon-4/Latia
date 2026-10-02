package pe.edu.upc.latia_lati.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.latia_lati.entities.MedicationSchedule;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
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

    @GetMapping("/tratamiento/{treatmentId}")
    public ResponseEntity<List<MedicationSchedule>> listarPorTratamiento(
            @PathVariable Long treatmentId) {
        return ResponseEntity.ok(repository.findByTreatment_Id(treatmentId));
    }

    @PostMapping
    public ResponseEntity<MedicationSchedule> registrar(@RequestBody MedicationSchedule schedule) {
        schedule.setId(null);
        MedicationSchedule saved = repository.save(schedule);
        return ResponseEntity.created(URI.create("/api/medicationschedules/" + saved.getId())).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationSchedule> buscarPorId(@PathVariable Long id) {
        MedicationSchedule schedule = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un horario de medicamento con el id: " + id));
        return ResponseEntity.ok(schedule);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationSchedule> actualizar(
            @PathVariable Long id,
            @RequestBody MedicationSchedule request) {
        MedicationSchedule schedule = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un horario de medicamento con el id: " + id));

        schedule.setTreatment(request.getTreatment());
        schedule.setReminderTime(request.getReminderTime());
        schedule.setIntervalHours(request.getIntervalHours());
        return ResponseEntity.ok(repository.save(schedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "No existe un horario de medicamento con el id: " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

package pe.edu.upc.latia_lati.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.latia_lati.entities.ExamResult;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.repositories.IExamResultRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/examresults")
public class ExamResultController {
    private final IExamResultRepository repository;

    public ExamResultController(IExamResultRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<ExamResult>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("/registro-clinico/{clinicalRecordId}")
    public ResponseEntity<List<ExamResult>> listarPorRegistroClinico(
            @PathVariable Long clinicalRecordId) {
        return ResponseEntity.ok(
                repository.findByClinicalRecord_IdClinicalRecord(clinicalRecordId));
    }

    @PostMapping
    public ResponseEntity<ExamResult> registrar(@RequestBody ExamResult examResult) {
        examResult.setId(null);
        ExamResult saved = repository.save(examResult);
        return ResponseEntity.created(URI.create("/api/examresults/" + saved.getId())).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResult> buscarPorId(@PathVariable Long id) {
        ExamResult examResult = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un resultado de examen con el id: " + id));
        return ResponseEntity.ok(examResult);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamResult> actualizar(
            @PathVariable Long id,
            @RequestBody ExamResult request) {
        ExamResult examResult = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un resultado de examen con el id: " + id));

        examResult.setClinicalRecord(request.getClinicalRecord());
        examResult.setParameterName(request.getParameterName());
        examResult.setResultValue(request.getResultValue());
        examResult.setUnit(request.getUnit());
        return ResponseEntity.ok(repository.save(examResult));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "No existe un resultado de examen con el id: " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

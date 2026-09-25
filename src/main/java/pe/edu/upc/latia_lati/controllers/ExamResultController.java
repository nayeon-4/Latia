package pe.edu.upc.latia_lati.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.latia_lati.entities.ExamResult;
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

    @PostMapping
    public ResponseEntity<ExamResult> registrar(@RequestBody ExamResult examResult) {
        ExamResult saved = repository.save(examResult);
        return ResponseEntity.created(URI.create("/api/examresults/" + saved.getId())).body(saved);
    }
}

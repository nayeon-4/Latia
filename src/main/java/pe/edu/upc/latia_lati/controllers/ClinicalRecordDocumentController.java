package pe.edu.upc.latia_lati.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.latia_lati.entities.ClinicalRecordDocument;
import pe.edu.upc.latia_lati.repositories.IClinicalRecordDocumentRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/clinicalrecorddocuments")
public class ClinicalRecordDocumentController {
    private final IClinicalRecordDocumentRepository repository;

    public ClinicalRecordDocumentController(IClinicalRecordDocumentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<ClinicalRecordDocument>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<ClinicalRecordDocument> registrar(@RequestBody ClinicalRecordDocument document) {
        ClinicalRecordDocument saved = repository.save(document);
        return ResponseEntity.created(URI.create("/api/clinicalrecorddocuments/" + saved.getId())).body(saved);
    }
}

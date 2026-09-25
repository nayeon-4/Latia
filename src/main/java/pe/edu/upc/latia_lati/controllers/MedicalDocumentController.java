package pe.edu.upc.latia_lati.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.latia_lati.entities.MedicalDocument;
import pe.edu.upc.latia_lati.repositories.IMedicalDocumentRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medicaldocuments")
public class MedicalDocumentController {
    private final IMedicalDocumentRepository repository;

    public MedicalDocumentController(IMedicalDocumentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<MedicalDocument>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<MedicalDocument> registrar(@RequestBody MedicalDocument document) {
        MedicalDocument saved = repository.save(document);
        return ResponseEntity.created(URI.create("/api/medicaldocuments/" + saved.getId())).body(saved);
    }
}

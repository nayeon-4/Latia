package pe.edu.upc.latia_lati.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.latia_lati.entities.ClinicalRecordDocument;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
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

    @GetMapping("/registro-clinico/{clinicalRecordId}")
    public ResponseEntity<List<ClinicalRecordDocument>> listarPorRegistroClinico(
            @PathVariable Long clinicalRecordId) {
        return ResponseEntity.ok(
                repository.findByClinicalRecord_IdClinicalRecord(clinicalRecordId));
    }

    @PostMapping
    public ResponseEntity<ClinicalRecordDocument> registrar(@RequestBody ClinicalRecordDocument document) {
        document.setId(null);
        ClinicalRecordDocument saved = repository.save(document);
        return ResponseEntity.created(URI.create("/api/clinicalrecorddocuments/" + saved.getId())).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicalRecordDocument> buscarPorId(@PathVariable Long id) {
        ClinicalRecordDocument document = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un documento de registro clínico con el id: " + id));
        return ResponseEntity.ok(document);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicalRecordDocument> actualizar(
            @PathVariable Long id,
            @RequestBody ClinicalRecordDocument request) {
        ClinicalRecordDocument document = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un documento de registro clínico con el id: " + id));

        document.setClinicalRecord(request.getClinicalRecord());
        document.setMedicalDocument(request.getMedicalDocument());
        return ResponseEntity.ok(repository.save(document));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "No existe un documento de registro clínico con el id: " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

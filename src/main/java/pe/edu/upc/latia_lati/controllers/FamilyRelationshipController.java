package pe.edu.upc.latia_lati.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.latia_lati.entities.FamilyRelationship;
import pe.edu.upc.latia_lati.repositories.IFamilyRelationshipRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/familyrelationships")
public class FamilyRelationshipController {
    private final IFamilyRelationshipRepository repository;

    public FamilyRelationshipController(IFamilyRelationshipRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ResponseEntity<List<FamilyRelationship>> listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping
    public ResponseEntity<FamilyRelationship> registrar(@RequestBody FamilyRelationship relationship) {
        FamilyRelationship saved = repository.save(relationship);
        return ResponseEntity.created(URI.create("/api/familyrelationships/" + saved.getId())).body(saved);
    }
}

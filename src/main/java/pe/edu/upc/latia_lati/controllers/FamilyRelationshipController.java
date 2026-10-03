package pe.edu.upc.latia_lati.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.latia_lati.entities.FamilyRelationship;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
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

    @GetMapping("/perfil/{healthProfileId}")
    public ResponseEntity<List<FamilyRelationship>> listarPorPerfilDeSalud(
            @PathVariable Long healthProfileId) {
        return ResponseEntity.ok(repository
                .findByOriginProfile_IdHealthProfileOrRelativeProfile_IdHealthProfile(
                        healthProfileId, healthProfileId));
    }

    @PostMapping
    public ResponseEntity<FamilyRelationship> registrar(@RequestBody FamilyRelationship relationship) {
        relationship.setId(null);
        FamilyRelationship saved = repository.save(relationship);
        return ResponseEntity.created(URI.create("/api/familyrelationships/" + saved.getId())).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FamilyRelationship> buscarPorId(@PathVariable Long id) {
        FamilyRelationship relationship = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una relación familiar con el id: " + id));
        return ResponseEntity.ok(relationship);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FamilyRelationship> actualizar(
            @PathVariable Long id,
            @RequestBody FamilyRelationship request) {
        FamilyRelationship relationship = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una relación familiar con el id: " + id));

        relationship.setOriginProfile(request.getOriginProfile());
        relationship.setRelativeProfile(request.getRelativeProfile());
        relationship.setRelationshipType(request.getRelationshipType());
        return ResponseEntity.ok(repository.save(relationship));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "No existe una relación familiar con el id: " + id);
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

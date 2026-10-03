package pe.edu.upc.latia_lati.controllers;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.latia_lati.dtos.*;
import pe.edu.upc.latia_lati.serviceinterfaces.IHealthProfileService;

@RestController
@RequestMapping("/api/healthprofiles")
public class HealthProfileController {
    private final IHealthProfileService profiles;
    public HealthProfileController(IHealthProfileService profiles) { this.profiles = profiles; }
    @PostMapping
    public ResponseEntity<HealthProfileDTO> registrar(@Valid @RequestBody HealthProfileRequestDTO request) {
        HealthProfileDTO dto = profiles.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getIdHealthProfile()).toUri();
        return ResponseEntity.created(location).body(dto);
    }
    @GetMapping
    public ResponseEntity<List<HealthProfileDTO>> listar() { return ResponseEntity.ok(profiles.list()); }
    @GetMapping("/{id}")
    public ResponseEntity<HealthProfileDTO> buscarPorId(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(profiles.find(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<HealthProfileDTO> actualizar(@PathVariable @Positive Long id, @Valid @RequestBody HealthProfileRequestDTO request) {
        return ResponseEntity.ok(profiles.update(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @Positive Long id) {
        profiles.delete(id); return ResponseEntity.noContent().build();
    }
}

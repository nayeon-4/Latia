package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.edu.upc.latia_lati.dtos.*;
import pe.edu.upc.latia_lati.serviceinterfaces.IHealthProfileService;
import java.net.URI;
import java.util.List;

@io.swagger.v3.oas.annotations.responses.ApiResponses({
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Datos o parámetros inválidos", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = ErrorResponse.class))),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Falta iniciar sesión o el token no es válido"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "403", description = "No tienes permiso"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Recurso inexistente o búsqueda sin resultados"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Duplicado o dependencias que impiden la operación"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Error interno"),
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "503", description = "Base de datos no disponible")
})
@RestController
@RequestMapping("/api/healthprofiles")
@Tag(name = "Perfiles de salud", description = "HU06–HU10: CRUD y consultas de los perfiles propios")
public class HealthProfileController {
    private final IHealthProfileService hpS;
    public HealthProfileController(IHealthProfileService hpS) { this.hpS = hpS; }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Registro creado")
    @PostMapping
    @Operation(summary = "HU06: Crear perfil de salud", description = "El propietario proviene del JWT. Para un dependiente, idHolderUser=null.")
    public ResponseEntity<HealthProfileDTO> registrar(@Valid @RequestBody HealthProfileRequestDTO request) {
        HealthProfileDTO result = hpS.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(result.getIdHealthProfile()).toUri();
        return ResponseEntity.created(location).body(result);
    }

    @GetMapping
    @Operation(summary = "HU07: Listar mis perfiles")
    public ResponseEntity<List<HealthProfileDTO>> listar() { return ResponseEntity.ok(hpS.list()); }

    @GetMapping("/{id}")
    @Operation(summary = "HU08: Buscar perfil por ID", description = "Un perfil inexistente o ajeno devuelve 404.")
    public ResponseEntity<HealthProfileDTO> buscarPorId(@PathVariable @Positive(message = "El ID debe ser positivo") Long id) {
        return ResponseEntity.ok(hpS.find(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "HU09: Actualizar perfil", description = "El ID proviene de la URL. No permite transferir el propietario.")
    public ResponseEntity<HealthProfileDTO> actualizar(@PathVariable @Positive(message = "El ID debe ser positivo") Long id,
                                                       @Valid @RequestBody HealthProfileRequestDTO request) {
        return ResponseEntity.ok(hpS.update(id, request));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Registro eliminado; sin cuerpo")
    @DeleteMapping("/{id}")
    @Operation(summary = "HU10: Eliminar perfil", description = "Devuelve 409 si hay contactos, documentos u otros registros vinculados.")
    public ResponseEntity<Void> eliminar(@PathVariable @Positive(message = "El ID debe ser positivo") Long id) {
        hpS.delete(id); return ResponseEntity.noContent().build();
    }

    @GetMapping("/consulta-simple")
    @Operation(summary = "Consulta simple: mis perfiles por tipo de sangre", description = "Usa findByOwnerUser_IdUserAndBloodTypeOrderByIdHealthProfileAsc. Swagger codifica el + de O+, A+, etc.")
    public ResponseEntity<List<HealthProfileDTO>> buscarPorSangre(
            @RequestParam @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Usa A+, A-, B+, B-, AB+, AB-, O+ u O-") String bloodType) {
        return ResponseEntity.ok(hpS.findByBloodType(bloodType));
    }

    @GetMapping("/consulta-nativa")
    @Operation(summary = "Consulta nativa: mis perfiles activos", description = "SQL con INNER JOIN. El propietario se obtiene del JWT; no acepta un ID de otra cuenta.")
    public ResponseEntity<List<HealthProfileDTO>> perfilesActivos() { return ResponseEntity.ok(hpS.activeProfiles()); }
}

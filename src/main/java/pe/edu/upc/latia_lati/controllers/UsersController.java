package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import pe.edu.upc.latia_lati.dtos.*;
import pe.edu.upc.latia_lati.serviceinterfaces.IUsersService;
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
@RequestMapping("/api/users")
@Tag(name = "Usuarios", description = "HU01–HU05: CRUD y consultas de usuarios")
public class UsersController {
    private final IUsersService uS;
    public UsersController(IUsersService uS) { this.uS = uS; }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Registro creado")
    @PostMapping({"", "/registro"})
    @SecurityRequirements
    @Operation(summary = "HU01: Registrar usuario", description = "Público. La contraseña se cifra y el rol USER se asigna en el servidor. /registro se conserva como alias.")
    public ResponseEntity<UsersResponseDTO> registrar(@Valid @RequestBody UsersRequestDTO request) {
        UsersResponseDTO result = uS.register(request);
        URI location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/users/{id}").buildAndExpand(result.getIdUser()).toUri();
        return ResponseEntity.created(location).body(result);
    }

    @GetMapping
    @Operation(summary = "HU02: Listar usuarios", description = "Requiere administrador.")
    public ResponseEntity<List<UsersResponseDTO>> listar() { return ResponseEntity.ok(uS.list()); }

    @GetMapping("/{id}")
    @Operation(summary = "HU03: Buscar usuario por ID", description = "Cuenta propia o administrador.")
    public ResponseEntity<UsersResponseDTO> buscarPorId(@PathVariable @Positive(message = "El ID debe ser positivo") Long id) {
        return ResponseEntity.ok(uS.find(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "HU04: Actualizar usuario", description = "El ID proviene de la URL. No modifica contraseña ni roles.")
    public ResponseEntity<UsersResponseDTO> actualizar(@PathVariable @Positive(message = "El ID debe ser positivo") Long id,
                                                     @Valid @RequestBody UpdateUserDTO request) {
        return ResponseEntity.ok(uS.update(id, request));
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Registro eliminado; sin cuerpo")
    @DeleteMapping("/{id}")
    @Operation(summary = "HU05: Eliminar usuario", description = "Devuelve 409 si existen registros dependientes; no los borra en cascada.")
    public ResponseEntity<Void> eliminar(@PathVariable @Positive(message = "El ID debe ser positivo") Long id) {
        uS.delete(id); return ResponseEntity.noContent().build();
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Contraseña actualizada; sin cuerpo")
    @PutMapping("/{id}/password")
    @Operation(summary = "Cambiar la contraseña propia", description = "Requiere la contraseña actual. Respuesta correcta: 204.")
    public ResponseEntity<Void> cambiarPassword(@PathVariable @Positive(message = "El ID debe ser positivo") Long id,
                                               @Valid @RequestBody ChangePasswordDTO request) {
        uS.changePassword(id, request); return ResponseEntity.noContent().build();
    }

    @GetMapping("/consulta-simple")
    @Operation(summary = "Consulta simple: usuarios por estado", description = "Usa findByActiveOrderByIdUserAsc. Requiere administrador. active=true o false.")
    public ResponseEntity<List<UsersResponseDTO>> buscarPorEstado(@RequestParam Boolean active) {
        return ResponseEntity.ok(uS.findByActive(active));
    }

    @GetMapping("/consulta-nativa")
    @Operation(summary = "Consulta nativa: total de perfiles por usuario", description = "SQL con LEFT JOIN y COUNT; incluye cuentas con cero perfiles. Requiere administrador.")
    public ResponseEntity<List<CountHealthProfilesByUserDTO>> contarPerfiles() {
        return ResponseEntity.ok(uS.countProfilesByUser());
    }
}

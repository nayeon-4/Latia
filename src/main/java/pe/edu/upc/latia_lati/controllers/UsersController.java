package pe.edu.upc.latia_lati.controllers;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.latia_lati.dtos.*;
import pe.edu.upc.latia_lati.serviceinterfaces.IUsersService;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final IUsersService users;
    public UsersController(IUsersService users) { this.users = users; }

    @PostMapping @SecurityRequirements
    public ResponseEntity<UsersResponseDTO> registrar(@Valid @RequestBody UsersRequestDTO request) {
        UsersResponseDTO dto = users.register(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getIdUser()).toUri();
        return ResponseEntity.created(location).body(dto);
    }
    @GetMapping
    public ResponseEntity<List<UsersResponseDTO>> listar() { return ResponseEntity.ok(users.list()); }

    @GetMapping("/{id}")
    public ResponseEntity<UsersResponseDTO> buscarPorId(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(users.find(id));
    }
    @PutMapping("/{id}")
    public ResponseEntity<UsersResponseDTO> actualizar(@PathVariable @Positive Long id, @Valid @RequestBody UpdateUserDTO request) {
        return ResponseEntity.ok(users.update(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable @Positive Long id) {
        users.delete(id); return ResponseEntity.noContent().build();
    }
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> cambiarPassword(@PathVariable @Positive Long id, @Valid @RequestBody ChangePasswordDTO request) {
        users.changePassword(id, request); return ResponseEntity.noContent().build();
    }
}

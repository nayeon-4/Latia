package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import pe.edu.upc.latia_lati.entities.*;
import pe.edu.upc.latia_lati.dtos.UserDTO;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IUserService;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final IUserService uS;
    private final ModelMapper modelMapper;

    public UserController(IUserService uS, ModelMapper modelMapper) {
        this.uS = uS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> listar() {

        List<UserDTO> lista = uS.list()
                .stream()
                .map(user -> modelMapper.map(user, UserDTO.class))
                .toList();

        return ResponseEntity.ok(lista);
    }
    @PostMapping
    public ResponseEntity<UserDTO> registrar(
            @Valid @RequestBody UserDTO dto) {

        User u = modelMapper.map(dto, User.class);

        uS.insert(u);

        UserDTO responseDTO =
                modelMapper.map(u, UserDTO.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(u.getIdUser())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> buscarPorId(
            @PathVariable Long id) {

        User user = uS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un usuario con el id: " + id
                        )
                );

        UserDTO dto = modelMapper.map(user, UserDTO.class);

        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<UserDTO> actualizar(
            @Valid @RequestBody UserDTO dto) {

        User u = uS.listId(dto.getIdUser())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un usuario con el id: "
                                        + dto.getIdUser()
                        )
                );

        u.setFirstName(dto.getFirstName());
        u.setLastName(dto.getLastName());
        u.setEmail(dto.getEmail());
        u.setPasswordHash(dto.getPasswordHash());
        u.setActive(dto.getActive());
        u.setPreferredTheme(dto.getPreferredTheme());

        uS.update(u);

        UserDTO responseDTO =
                modelMapper.map(u, UserDTO.class);

        return ResponseEntity.ok(responseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        User u = uS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un usuario con el id: " + id
                        )
                );
        uS.delete(u.getIdUser());
        return ResponseEntity.noContent().build();
    }
}

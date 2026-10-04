package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

import pe.edu.upc.latia_lati.dtos.UsersRequestDTO;
import pe.edu.upc.latia_lati.entities.*;
import pe.edu.upc.latia_lati.dtos.UsersResponseDTO;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IUsersService;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final IUsersService uS;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    public UsersController(IUsersService uS, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.uS = uS;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public ResponseEntity<List<UsersResponseDTO>> listar() {

        List<UsersResponseDTO> lista = uS.list()
                .stream()
                .map(user -> modelMapper.map(user, UsersResponseDTO.class))
                .toList();

        return ResponseEntity.ok(lista);
    }
    @PostMapping("/registro")
    public ResponseEntity<UsersResponseDTO> registrar(
            @Valid @RequestBody UsersRequestDTO request) {

        Users u = modelMapper.map(request, Users.class);

        u.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));

        uS.insert(u);

        UsersResponseDTO responseDTO = modelMapper.map(u, UsersResponseDTO.class);

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
    public ResponseEntity<UsersResponseDTO> buscarPorId(
            @PathVariable Long id) {

        Users user = uS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un usuario con el id: " + id
                        )
                );

        UsersResponseDTO dto = modelMapper.map(user, UsersResponseDTO.class);

        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<UsersResponseDTO> actualizar(
            @Valid @RequestBody UsersRequestDTO request) {

        Users u = uS.listId(request.getIdUser())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un usuario con el id: "
                                        + request.getIdUser()
                        )
                );

        u.setFirstName(request.getFirstName());
        u.setLastName(request.getLastName());
        u.setUsername(request.getUsername());
        u.setEmail(request.getEmail());
        u.setActive(request.getActive());
        u.setPasswordHash(passwordEncoder.encode(request.getPasswordHash()));

        uS.update(u);

        UsersResponseDTO responseDTO =
                modelMapper.map(u, UsersResponseDTO.class);

        return ResponseEntity.ok(responseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Users u = uS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un usuario con el id: " + id
                        )
                );
        uS.delete(u.getIdUser());
        return ResponseEntity.noContent().build();
    }
}
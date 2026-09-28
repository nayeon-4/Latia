package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import pe.edu.upc.latia_lati.entities.*;
import pe.edu.upc.latia_lati.dtos.UsersDTO;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IUsersService;

@RestController
@RequestMapping("/api/users")
public class UsersController {
    private final IUsersService uS;
    private final ModelMapper modelMapper;

    public UsersController(IUsersService uS, ModelMapper modelMapper) {
        this.uS = uS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<UsersDTO>> listar() {

        List<UsersDTO> lista = uS.list()
                .stream()
                .map(user -> modelMapper.map(user, UsersDTO.class))
                .toList();

        return ResponseEntity.ok(lista);
    }
    @PostMapping
    public ResponseEntity<UsersDTO> registrar(
            @Valid @RequestBody UsersDTO dto) {

        Users u = modelMapper.map(dto, Users.class);

        uS.insert(u);

        UsersDTO responseDTO =
                modelMapper.map(u, UsersDTO.class);

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
    public ResponseEntity<UsersDTO> buscarPorId(
            @PathVariable Long id) {

        Users user = uS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un usuario con el id: " + id
                        )
                );

        UsersDTO dto = modelMapper.map(user, UsersDTO.class);

        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<UsersDTO> actualizar(
            @Valid @RequestBody UsersDTO dto) {

        Users u = uS.listId(dto.getIdUser())
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

        uS.update(u);

        UsersDTO responseDTO =
                modelMapper.map(u, UsersDTO.class);

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

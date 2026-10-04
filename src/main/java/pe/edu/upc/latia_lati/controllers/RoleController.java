package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pe.edu.upc.latia_lati.dtos.RoleDTO;
import pe.edu.upc.latia_lati.entities.Role;
import pe.edu.upc.latia_lati.entities.Users;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IRoleService;
import pe.edu.upc.latia_lati.serviceinterfaces.IUsersService;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    private final IRoleService rS;
    private final IUsersService uS;
    private final ModelMapper modelMapper;

    public RoleController(IRoleService rS, IUsersService uS, ModelMapper modelMapper) {
        this.rS = rS;
        this.uS = uS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> listar() {

        List<RoleDTO> lista = rS.list()
                .stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<RoleDTO> registrar(
            @Valid @RequestBody RoleDTO dto) {

        Users user = uS.listId(dto.getIdUser())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un usuario con el id: "
                                        + dto.getIdUser()
                        )
                );

        Role role = modelMapper.map(dto, Role.class);

        role.setUser(user);

        rS.insert(role);

        RoleDTO responseDTO =
                modelMapper.map(role, RoleDTO.class);

        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> buscarPorId(
            @PathVariable Long id) {

        Role role = rS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un rol con el id: " + id
                        )
                );

        RoleDTO dto = modelMapper.map(role, RoleDTO.class);

        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<RoleDTO> actualizar(
            @Valid @RequestBody RoleDTO dto) {

        Role role = rS.listId(dto.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un rol con el id: "
                                        + dto.getId()
                        )
                );

        Users user = uS.listId(dto.getIdUser())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un usuario con el id: "
                                        + dto.getIdUser()
                        )
                );

        role.setRol(dto.getRol());
        role.setUser(user);

        rS.update(role);

        RoleDTO responseDTO =
                modelMapper.map(role, RoleDTO.class);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        Role role = rS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un rol con el id: " + id
                        )
                );

        rS.delete(role.getId());

        return ResponseEntity.noContent().build();
    }
}

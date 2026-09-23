package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.latia_lati.dtos.HealthProfileDTO;
import pe.edu.upc.latia_lati.entities.HealthProfile;
import pe.edu.upc.latia_lati.entities.User;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IHealthProfileService;
import pe.edu.upc.latia_lati.serviceinterfaces.IUserService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/healthprofiles")
public class HealthProfileController {

    private final IHealthProfileService hpS;
    private final IUserService uS;
    private final ModelMapper modelMapper;

    public HealthProfileController(IHealthProfileService hpS, IUserService uS, ModelMapper modelMapper) {
        this.hpS = hpS;
        this.uS = uS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<HealthProfileDTO>> listar() {

        List<HealthProfileDTO> lista = hpS.list()
                .stream()
                .map(healthProfile ->
                        modelMapper.map(
                                healthProfile,
                                HealthProfileDTO.class
                        )
                )
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<HealthProfileDTO> registrar(
            @Valid @RequestBody HealthProfileDTO dto) {

        // 1. Verificar que el usuario propietario exista
        Optional<User> owner = uS.listId(dto.getIdOwnerUser());

        if (owner.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe el usuario propietario con el id: "
                            + dto.getIdOwnerUser()
            );
        }

        // 2. Verificar el usuario titular si fue enviado
        User holder = null;

        if (dto.getIdHolderUser() != null) {
            Optional<User> holderOptional =
                    uS.listId(dto.getIdHolderUser());

            if (holderOptional.isEmpty()) {
                throw new ResourceNotFoundException(
                        "No existe el usuario titular con el id: "
                                + dto.getIdHolderUser()
                );
            }

            holder = holderOptional.get();
        }

        // 3. Convertir DTO a entidad
        HealthProfile hp =
                modelMapper.map(dto, HealthProfile.class);

        // 4. Asignar relaciones
        hp.setOwnerUser(owner.get());
        hp.setHolderUser(holder);

        // 5. Guardar
        hpS.insert(hp);

        // 6. Convertir a DTO
        HealthProfileDTO responseDTO =
                modelMapper.map(hp, HealthProfileDTO.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(hp.getIdHealthProfile())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HealthProfileDTO> buscarPorId(
            @PathVariable Long id) {

        HealthProfile hp = hpS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un perfil de salud con el id: " + id
                        )
                );

        HealthProfileDTO dto = modelMapper.map(hp, HealthProfileDTO.class);

        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<HealthProfileDTO> actualizar(
            @Valid @RequestBody HealthProfileDTO dto) {

        // 1. Verificar que el perfil de salud exista
        Optional<HealthProfile> existente =
                hpS.listId(dto.getIdHealthProfile());

        if (existente.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un perfil de salud con el id: "
                            + dto.getIdHealthProfile()
            );
        }

        // 2. Verificar que el usuario propietario exista
        Optional<User> owner =
                uS.listId(dto.getIdOwnerUser());

        if (owner.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe el usuario propietario con el id: " + dto.getIdOwnerUser()
            );
        }

        // 3. Verificar el usuario titular si fue enviado
        User holder = null;

        if (dto.getIdHolderUser() != null) {
            Optional<User> holderOptional =
                    uS.listId(dto.getIdHolderUser());

            if (holderOptional.isEmpty()) {
                throw new ResourceNotFoundException(
                        "No existe el usuario titular con el id: " + dto.getIdHolderUser()
                );
            }

            holder = holderOptional.get();
        }

        // 4. Obtener el perfil existente
        HealthProfile hp = existente.get();

        // 5. Actualizar sus campos
        hp.setFirstName(dto.getFirstName());
        hp.setLastName(dto.getLastName());
        hp.setBirthDate(dto.getBirthDate());
        hp.setSex(dto.getSex());
        hp.setBloodType(dto.getBloodType());
        hp.setPhone(dto.getPhone());
        hp.setActive(dto.getActive());

        // 6. Asignar relaciones
        hp.setOwnerUser(owner.get());
        hp.setHolderUser(holder);

        // 7. Guardar
        hpS.update(hp);

        // 8. Convertir a DTO
        HealthProfileDTO responseDTO = modelMapper.map(hp, HealthProfileDTO.class);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        HealthProfile hp = hpS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un perfil de salud con el id: " + id
                        )
                );

        hpS.delete(hp.getIdHealthProfile());

        return ResponseEntity.noContent().build();
    }
}
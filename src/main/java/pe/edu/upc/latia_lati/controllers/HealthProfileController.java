package pe.edu.upc.latia_lati.controllers;

import io.swagger.v3.oas.annotations.Operation;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.latia_lati.dtos.HealthProfileDTO;
import pe.edu.upc.latia_lati.entities.HealthProfile;
import pe.edu.upc.latia_lati.entities.Users;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IHealthProfileService;
import pe.edu.upc.latia_lati.serviceinterfaces.IUsersService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/healthprofiles")
public class HealthProfileController {

    private final IHealthProfileService hpS;
    private final IUsersService uS;
    private final ModelMapper modelMapper;

    public HealthProfileController(IHealthProfileService hpS, IUsersService uS, ModelMapper modelMapper) {
        this.hpS = hpS;
        this.uS = uS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<HealthProfileDTO>> listar() {

        List<HealthProfileDTO> lista = hpS.list()
                .stream()
                .map(healthProfile -> {
                    HealthProfileDTO dto = modelMapper.map(healthProfile, HealthProfileDTO.class);

                    if (healthProfile.getOwnerUser() != null) {
                        dto.setIdOwnerUser(healthProfile.getOwnerUser().getIdUser());
                    }

                    if (healthProfile.getHolderUser() != null) {
                        dto.setIdHolderUser(healthProfile.getHolderUser().getIdUser());
                    }

                    return dto;
                })
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<HealthProfileDTO> registrar(
            @Valid @RequestBody HealthProfileDTO dto) {

        // 1. Verificar que el usuario propietario exista
        Optional<Users> owner = uS.listId(dto.getIdOwnerUser());

        if (owner.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe el usuario propietario con el id: "
                            + dto.getIdOwnerUser()
            );
        }

        // 2. Verificar el usuario titular si fue enviado
        Users holder = null;

        if (dto.getIdHolderUser() != null) {
            Optional<Users> holderOptional =
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
        HealthProfile hp = modelMapper.map(dto, HealthProfile.class);

        // 4. Asignar relaciones
        hp.setOwnerUser(owner.get());
        hp.setHolderUser(holder);

        // 5. Guardar
        hpS.insert(hp);

        // 6. Convertir a DTO
        HealthProfileDTO responseDTO = modelMapper.map(hp, HealthProfileDTO.class);

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

        Optional<HealthProfile> existente =
                hpS.listId(dto.getIdHealthProfile());

        if (existente.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un perfil de salud con el id: "
                            + dto.getIdHealthProfile()
            );
        }

        Optional<Users> owner =
                uS.listId(dto.getIdOwnerUser());

        if (owner.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe el usuario propietario con el id: " + dto.getIdOwnerUser()
            );
        }

        Users holder = null;

        if (dto.getIdHolderUser() != null) {
            Optional<Users> holderOptional =
                    uS.listId(dto.getIdHolderUser());

            if (holderOptional.isEmpty()) {
                throw new ResourceNotFoundException(
                        "No existe el usuario titular con el id: " + dto.getIdHolderUser()
                );
            }

            holder = holderOptional.get();
        }

        // Obtener el perfil existente
        HealthProfile hp = existente.get();

        // Actualiza los campos
        hp.setBirthDate(dto.getBirthDate());
        hp.setSex(dto.getSex());
        hp.setBloodType(dto.getBloodType());
        hp.setPhone(dto.getPhone());
        hp.setActive(dto.getActive());

        // Asignar relaciones
        hp.setOwnerUser(owner.get());
        hp.setHolderUser(holder);

        // Guardar
        hpS.update(hp);

        // Convertir a DTO
        HealthProfileDTO responseDTO = modelMapper.map(hp, HealthProfileDTO.class);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        HealthProfile hp = hpS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un perfil de salud con el id: " + id
                        )
                );

        hpS.delete(hp.getIdHealthProfile());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/consulta-simple")
    @Operation(summary = "Consulta simple: mis perfiles por tipo de sangre", description = "Escribe O+, A-, etc. en el parámetro bloodType. Solo devuelve perfiles propios.")
    public ResponseEntity<List<HealthProfileDTO>> buscarPorTipoSangre(@RequestParam String bloodType) {
        List<HealthProfileDTO> lista = hpS.obtenerPorTipoSangre(bloodType)
                .stream()
                .map(this::convertirConsultaDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/consulta-nativa")
    @Operation(summary = "Consulta nativa: mis perfiles activos", description = "SQL con INNER JOIN y filtro por el propietario autenticado.")
    public ResponseEntity<List<HealthProfileDTO>> listarActivosPropios() {
        List<HealthProfileDTO> lista = hpS.listarActivosPropios()
                .stream()
                .map(this::convertirConsultaDTO)
                .toList();
        return ResponseEntity.ok(lista);
    }

    // Mismo mapeo utilizado por el listado de main; aplicado solo a las consultas nuevas.
    private HealthProfileDTO convertirConsultaDTO(HealthProfile hp) {
        HealthProfileDTO dto = modelMapper.map(hp, HealthProfileDTO.class);
        dto.setIdOwnerUser(hp.getOwnerUser().getIdUser());
        dto.setIdHolderUser(hp.getHolderUser() == null ? null : hp.getHolderUser().getIdUser());
        return dto;
    }

}
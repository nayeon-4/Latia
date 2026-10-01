package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import pe.edu.upc.latia_lati.entities.*;
import pe.edu.upc.latia_lati.dtos.EmergencyCardDTO;
import pe.edu.upc.latia_lati.dtos.EmergencyContactDTO;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IEmergencyContactService;
import pe.edu.upc.latia_lati.serviceinterfaces.IHealthProfileService;

@RestController
@RequestMapping("/api/emergencycontacts")
public class EmergencyContactController {

    private final IEmergencyContactService ecS;
    private final IHealthProfileService hpS;
    private final ModelMapper modelMapper;

    public EmergencyContactController(IEmergencyContactService ecS, IHealthProfileService hpS, ModelMapper modelMapper) {
        this.ecS = ecS;
        this.hpS = hpS;
        this.modelMapper = modelMapper;
    }

    // HU12: Listar contactos de emergencia
    @GetMapping
    public ResponseEntity<List<EmergencyContactDTO>> listar() {

        List<EmergencyContact> contactos = ecS.list();

        // CA03: informar el error cuando no existan registros
        if (contactos.isEmpty()) {
            throw new ResourceNotFoundException("No existen contactos de emergencia registrados");
        }

        List<EmergencyContactDTO> lista = contactos
                .stream()
                .map(ec -> convertirDTO(ec))
                .toList();

        return ResponseEntity.ok(lista);
    }

    // HU11: Crear contacto de emergencia
    @PostMapping
    public ResponseEntity<EmergencyContactDTO> registrar(
            @Valid @RequestBody EmergencyContactDTO dto) {

        // 1. Verificar que el perfil de salud exista
        HealthProfile hp = hpS.listId(dto.getIdHealthProfile())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un perfil de salud con el id: " + dto.getIdHealthProfile()
                        ));

        // 2. Convertir DTO a entidad
        EmergencyContact ec = modelMapper.map(dto, EmergencyContact.class);

        // 3. El id lo genera la base de datos; si llega en el body se ignora para no sobrescribir un contacto existente
        ec.setIdEmergencyContact(null);
        ec.setHealthProfile(hp);

        // 4. Guardar
        ecS.insert(ec);

        EmergencyContactDTO responseDTO = convertirDTO(ec);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ec.getIdEmergencyContact())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    // HU13: Encontrar contacto de emergencia por Id
    @GetMapping("/{id}")
    public ResponseEntity<EmergencyContactDTO> buscarPorId(
            @PathVariable Long id) {

        EmergencyContact ec = ecS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un contacto de emergencia con el id: " + id
                        ));

        return ResponseEntity.ok(convertirDTO(ec));
    }

    // HU14: Actualizar contacto de emergencia por Id
    @PutMapping("/{id}")
    public ResponseEntity<EmergencyContactDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EmergencyContactDTO dto) {

        // 1. Verificar que el contacto exista
        EmergencyContact ec = ecS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un contacto de emergencia con el id: " + id
                        ));

        // 2. Verificar que el perfil de salud exista
        HealthProfile hp = hpS.listId(dto.getIdHealthProfile())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un perfil de salud con el id: " + dto.getIdHealthProfile()
                        ));

        // 3. Copiar los datos editables (createdAt no se toca)
        ec.setHealthProfile(hp);
        ec.setName(dto.getName());
        ec.setPhone(dto.getPhone());
        ec.setRelationship(dto.getRelationship());
        ec.setPrimaryContact(dto.getPrimaryContact());

        // 4. Guardar
        ecS.update(ec);

        return ResponseEntity.ok(convertirDTO(ec));
    }

    // HU15: Borrar contacto de emergencia por Id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        // CA02: verificar que el contacto exista antes de eliminarlo
        EmergencyContact ec = ecS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un contacto de emergencia con el id: " + id
                        ));

        ecS.delete(ec.getIdEmergencyContact());

        return ResponseEntity.noContent().build();
    }

    // HU68: Listar contactos de emergencia principales (reporte simple)
    @GetMapping("/principales")
    public ResponseEntity<List<EmergencyContactDTO>> listarPrincipales() {

        // Si no hay coincidencias se devuelve la lista vacía
        List<EmergencyContactDTO> lista = ecS.listarPrincipales()
                .stream()
                .map(ec -> convertirDTO(ec))
                .toList();

        return ResponseEntity.ok(lista);
    }

    // HU69: Ficha de emergencia por perfil de salud (reporte con JOIN)
    @GetMapping("/fichas")
    public ResponseEntity<List<EmergencyCardDTO>> fichaEmergencia() {

        List<EmergencyCardDTO> lista = new ArrayList<>();

        for (Object[] item : ecS.fichaEmergencia()) {
            EmergencyCardDTO dto = new EmergencyCardDTO();
            dto.setIdHealthProfile(((Number) item[0]).longValue());
            dto.setPatientName((String) item[1] + " " + (String) item[2]);
            dto.setBloodType((String) item[3]);
            dto.setContactName((String) item[4]);
            dto.setContactPhone((String) item[5]);
            dto.setRelationship((String) item[6]);
            lista.add(dto);
        }

        return ResponseEntity.ok(lista);
    }

    private EmergencyContactDTO convertirDTO(EmergencyContact ec) {
        EmergencyContactDTO dto = modelMapper.map(ec, EmergencyContactDTO.class);
        dto.setIdHealthProfile(ec.getHealthProfile().getIdHealthProfile());
        return dto;
    }
}
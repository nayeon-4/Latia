package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import pe.edu.upc.latia_lati.entities.*;
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

    @GetMapping
    public ResponseEntity<List<EmergencyContactDTO>> listar() {

        List<EmergencyContactDTO> lista = ecS.list()
                .stream()
                .map(ec -> modelMapper.map(ec, EmergencyContactDTO.class))
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<EmergencyContactDTO> registrar(
            @Valid @RequestBody EmergencyContactDTO dto) {

        HealthProfile hp = hpS.listId(dto.getIdHealthProfile())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un perfil de salud con el id: " + dto.getIdHealthProfile()
                        ));

        EmergencyContact ec =
                modelMapper.map(dto, EmergencyContact.class);

        ec.setHealthProfile(hp);

        ecS.insert(ec);

        EmergencyContactDTO responseDTO =
                modelMapper.map(ec, EmergencyContactDTO.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(ec.getIdEmergencyContact())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmergencyContactDTO> buscarPorId(
            @PathVariable Long id) {

        EmergencyContact ec = ecS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un contacto de emergencia con el id: " + id
                        ));

        EmergencyContactDTO dto = modelMapper.map(ec, EmergencyContactDTO.class);

        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<EmergencyContactDTO> actualizar(
            @Valid @RequestBody EmergencyContactDTO dto) {

        EmergencyContact ec = ecS.listId(dto.getIdEmergencyContact())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un contacto de emergencia con el id: " + dto.getIdEmergencyContact()
                        ));

        HealthProfile hp = hpS.listId(dto.getIdHealthProfile())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un perfil de salud con el id: " + dto.getIdHealthProfile()
                        ));

        ec.setHealthProfile(hp);
        ec.setName(dto.getName());
        ec.setPhone(dto.getPhone());
        ec.setRelationship(dto.getRelationship());
        ec.setPrimaryContact(dto.getPrimaryContact());

        ecS.update(ec);

        EmergencyContactDTO responseDTO =
                modelMapper.map(ec, EmergencyContactDTO.class);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        EmergencyContact ec = ecS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un contacto de emergencia con el id: " + id
                        ));

        ecS.delete(ec.getIdEmergencyContact());

        return ResponseEntity.noContent().build();
    }
}

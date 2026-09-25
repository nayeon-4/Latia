package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.latia_lati.dtos.MedicationsDTO;
import pe.edu.upc.latia_lati.entities.Medications;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IMedicationsService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationsController {
    private final IMedicationsService mS;
    private final ModelMapper modelMapper;

    public MedicationsController(IMedicationsService mS, ModelMapper modelMapper) {
        this.mS = mS;
        this.modelMapper = modelMapper;
    }
    @GetMapping
    public ResponseEntity<List<MedicationsDTO>> listar() {
        List<MedicationsDTO> lista = mS.list()
                .stream()
                .map(medication -> modelMapper.map(medication, MedicationsDTO.class))
                .toList();

        return ResponseEntity.ok(lista);
    }
    @PostMapping
    public ResponseEntity<MedicationsDTO> registrar(
            @Valid @RequestBody MedicationsDTO dto) {
        Medications m = modelMapper.map(dto, Medications.class);
        mS.insert(m);
        MedicationsDTO responseDTO =
                modelMapper.map(m, MedicationsDTO.class);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(m.getIdMedications())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<MedicationsDTO> buscarPorId(
            @PathVariable Long id) {
        Medications m = mS.listId(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el medicamento con el id: " + id));
        MedicationsDTO dto = modelMapper.map(m, MedicationsDTO.class);
        return ResponseEntity.ok(dto);
    }
    @PutMapping
    public ResponseEntity<MedicationsDTO> actualizar(
            @Valid @RequestBody MedicationsDTO dto) {
        Medications existente = mS.listId(dto.getIdMedications())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un medicamento con el id: " + dto.getIdMedications()
                        )
                );
        Medications m = modelMapper.map(dto, Medications.class);
        m.setIdMedications(existente.getIdMedications());
        mS.update(m);
        MedicationsDTO responseDTO =
                modelMapper.map(m, MedicationsDTO.class);
        return ResponseEntity.ok(responseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Medications m = mS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un medicamento con el id: " + id
                        )
                );
        mS.delete(m.getIdMedications());
        return ResponseEntity.noContent().build();
    }
}

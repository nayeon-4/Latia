package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.latia_lati.dtos.MedicalConditionDTO;
import pe.edu.upc.latia_lati.entities.MedicalCondition;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IMedicalConditionService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medicalconditions")
public class MedicalConditionController {
    private final IMedicalConditionService mcS;
    private final ModelMapper modelMapper;

    public MedicalConditionController(IMedicalConditionService mcS, ModelMapper modelMapper) {
        this.mcS = mcS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<MedicalConditionDTO>> listar() {

        List<MedicalConditionDTO> lista = mcS.list()
                .stream()
                .map(medicalCondition -> modelMapper.map(medicalCondition, MedicalConditionDTO.class))
                .toList();

        return ResponseEntity.ok(lista);
    }
    @PostMapping
    public ResponseEntity<MedicalConditionDTO> registrar(
            @Valid @RequestBody MedicalConditionDTO dto) {

        MedicalCondition mc = modelMapper.map(dto, MedicalCondition.class);

        mcS.insert(mc);

        MedicalConditionDTO responseDTO =
                modelMapper.map(mc, MedicalConditionDTO.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(mc.getIdMedicalCondition())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalConditionDTO> buscarPorId(
            @PathVariable Long id) {

        MedicalCondition medicalCondition = mcS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe la condición médica con el id: " + id
                        )
                );

        MedicalConditionDTO dto = modelMapper.map(medicalCondition, MedicalConditionDTO.class);

        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<MedicalConditionDTO> actualizar(
            @Valid @RequestBody MedicalConditionDTO dto) {

        MedicalCondition mc = mcS.listId(dto.getIdMedicalCondition())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una condición médica con el id: "
                                + dto.getIdMedicalCondition()
                ));

        mc.setNameMedicalCondition(dto.getNameMedicalCondition());
        mc.setDescriptionMedicalCondition(dto.getDescriptionMedicalCondition());
        mc.setActive(dto.getActive());

        mcS.update(mc);

        MedicalConditionDTO responseDTO =
                modelMapper.map(mc, MedicalConditionDTO.class);

        return ResponseEntity.ok(responseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        MedicalCondition mc = mcS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe una condición médica con el id: " + id
                        )
                );
        mcS.delete(mc.getIdMedicalCondition());
        return ResponseEntity.noContent().build();
    }
}
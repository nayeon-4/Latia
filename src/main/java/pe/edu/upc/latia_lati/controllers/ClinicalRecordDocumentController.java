package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

import pe.edu.upc.latia_lati.dtos.ClinicalRecordDocumentDTO;
import pe.edu.upc.latia_lati.entities.ClinicalRecordDocument;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IClinicalRecordDocumentService;

@RestController
@RequestMapping("/api/clinicalrecorddocuments")
public class ClinicalRecordDocumentController {

    private final IClinicalRecordDocumentService crdS;
    private final ModelMapper modelMapper;

    public ClinicalRecordDocumentController(IClinicalRecordDocumentService crdS, ModelMapper modelMapper) {
        this.crdS = crdS;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<ClinicalRecordDocumentDTO>> listar() {
        List<ClinicalRecordDocumentDTO> lista = crdS.list()
                .stream()
                .map(document -> modelMapper.map(document, ClinicalRecordDocumentDTO.class))
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<ClinicalRecordDocumentDTO> registrar(
            @Valid @RequestBody ClinicalRecordDocumentDTO dto) {

        ClinicalRecordDocument document = modelMapper.map(dto, ClinicalRecordDocument.class);
        document.setId(null);
        crdS.insert(document);

        ClinicalRecordDocumentDTO responseDTO = modelMapper.map(document, ClinicalRecordDocumentDTO.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(document.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicalRecordDocumentDTO> buscarPorId(@PathVariable Long id) {
        ClinicalRecordDocument document = crdS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un documento de registro clínico con el id: " + id));

        ClinicalRecordDocumentDTO dto = modelMapper.map(document, ClinicalRecordDocumentDTO.class);

        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClinicalRecordDocumentDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody ClinicalRecordDocumentDTO dto) {

        ClinicalRecordDocument document = crdS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un documento de registro clínico con el id: " + id));

        modelMapper.map(dto, document);
        document.setId(id);

        crdS.update(document);

        ClinicalRecordDocumentDTO responseDTO = modelMapper.map(document, ClinicalRecordDocumentDTO.class);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ClinicalRecordDocument document = crdS.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un documento de registro clínico con el id: " + id));

        crdS.delete(document.getId());

        return ResponseEntity.noContent().build();
    }
}
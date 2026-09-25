package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.latia_lati.dtos.MedicalDocumentsDTO;
import pe.edu.upc.latia_lati.entities.HealthProfile;
import pe.edu.upc.latia_lati.entities.MedicalDocuments;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IHealthProfileService;
import pe.edu.upc.latia_lati.serviceinterfaces.IMedicalDocumentsService;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medical-documents")
public class MedicalDocumentsController {
    private final IMedicalDocumentsService mdS;
    private final ModelMapper modelMapper;
    private final IHealthProfileService hpS;

    public MedicalDocumentsController(IMedicalDocumentsService mdS, ModelMapper modelMapper, IHealthProfileService hpS) {
        this.mdS = mdS;
        this.modelMapper = modelMapper;
        this.hpS = hpS;
    }
    @GetMapping
    public ResponseEntity<List<MedicalDocumentsDTO>> listar() {

        List<MedicalDocumentsDTO> lista = mdS.list()
                .stream()
                .map(act -> modelMapper.map(act, MedicalDocumentsDTO.class))
                .toList();

        return ResponseEntity.ok(lista);
    }
    @PostMapping
    public ResponseEntity<MedicalDocumentsDTO> registrar(
            @Valid @RequestBody MedicalDocumentsDTO dto) {
        HealthProfile hp = hpS.listId(dto.getIdHealthProfile())
                .orElseThrow(() ->
                        new RuntimeException(
                                "No existe el perfil de salud con el id: " + dto.getIdHealthProfile()
                        )
                );
        MedicalDocuments md = modelMapper.map(dto, MedicalDocuments.class);
        md.setHealthProfile(hp);
        mdS.insert(md);

        MedicalDocumentsDTO responseDTO =
                modelMapper.map(md, MedicalDocumentsDTO.class);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(md.getIdMedicalDocuments())
                .toUri();
        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<MedicalDocumentsDTO> buscarPorId(
            @PathVariable Long id) {

        MedicalDocuments md = mdS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un documento médico con el id: " + id
                        )
                );

        MedicalDocumentsDTO dto = modelMapper.map(md, MedicalDocumentsDTO.class);
        return ResponseEntity.ok(dto);
    }
    @PutMapping
    public ResponseEntity<MedicalDocumentsDTO> actualizar(
            @Valid @RequestBody MedicalDocumentsDTO dto) {

        Optional<MedicalDocuments> existente = mdS.listId(dto.getIdMedicalDocuments());

        if (existente.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un documento médico con el id: " + dto.getIdMedicalDocuments()
            );
        }

        Optional<HealthProfile> hp = hpS.listId(dto.getIdHealthProfile());

        if (hp.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un perfil de salud con el id: " + dto.getIdHealthProfile()
            );
        }

        // 3. Obtener el documento médico existente
        MedicalDocuments medicalDocuments = existente.get();

        // 4. Actualizar sus campos
        medicalDocuments.setTitle(dto.getTitle());
        medicalDocuments.setDocumentType(dto.getDocumentType());
        medicalDocuments.setFileURL(dto.getFileURL());
        medicalDocuments.setDocumentDate(dto.getDocumentDate());

        // 5. Asignar el Perfil de Salud existente
        medicalDocuments.setHealthProfile(hp.get());

        // 6. Guardar
        mdS.update(medicalDocuments);

        // 7. Convertir a DTO
        MedicalDocumentsDTO responseDTO =
                modelMapper.map(medicalDocuments, MedicalDocumentsDTO.class);

        return ResponseEntity.ok(responseDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        MedicalDocuments md = mdS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un documento médico con el id: " + id
                        )
                );
        mdS.delete(md.getIdMedicalDocuments());
        return ResponseEntity.noContent().build();
    }
}

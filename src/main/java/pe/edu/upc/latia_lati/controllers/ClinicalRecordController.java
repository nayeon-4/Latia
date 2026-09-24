package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.latia_lati.dtos.ClinicalRecordDTO;
import pe.edu.upc.latia_lati.dtos.CountMedicalConditionDTO;
import pe.edu.upc.latia_lati.entities.ClinicalRecord;
import pe.edu.upc.latia_lati.entities.HealthProfile;
import pe.edu.upc.latia_lati.entities.MedicalCondition;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IClinicalRecordService;
import pe.edu.upc.latia_lati.serviceinterfaces.IHealthProfileService;
import pe.edu.upc.latia_lati.serviceinterfaces.IMedicalConditionService;

import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/apis/clinicalrecords")
public class ClinicalRecordController {
    private final IClinicalRecordService crS;
    private final ModelMapper modelMapper;
    private final IHealthProfileService hpS;
    private final IMedicalConditionService mcS;

    public ClinicalRecordController(IClinicalRecordService crS, ModelMapper modelMapper, IHealthProfileService hpS, IMedicalConditionService mcS) {
        this.crS = crS;
        this.modelMapper = modelMapper;
        this.hpS = hpS;
        this.mcS = mcS;
    }

    @GetMapping
    public ResponseEntity<List<ClinicalRecordDTO>> listar() {

        List<ClinicalRecordDTO> lista = crS.list()
                .stream()
                .map(act -> modelMapper.map(act, ClinicalRecordDTO.class))
                .toList();

        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<ClinicalRecordDTO> registrar(
            @Valid @RequestBody ClinicalRecordDTO dto) {
        HealthProfile hp = hpS.listId(dto.getIdHealthProfile())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el perfil de salud con el id: " + dto.getIdHealthProfile()
                        )
                );
        ClinicalRecord cr = modelMapper.map(dto, ClinicalRecord.class);
        cr.setHealthProfile(hp);

        MedicalCondition mc = mcS.listId(dto.getIdCondition())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe la condición médica con el id: " + dto.getIdCondition()
                        )
                );
        cr.setMedicalCondition(mc);

        crS.insert(cr);

        ClinicalRecordDTO responseDTO =
                modelMapper.map(cr, ClinicalRecordDTO.class);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cr.getIdClinicalRecord())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClinicalRecordDTO> buscarPorId(
            @PathVariable Long id) {

        ClinicalRecord cr = crS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe una historia clínica con el id: " + id
                        )
                );

        ClinicalRecordDTO dto = modelMapper.map(cr, ClinicalRecordDTO.class);
        return ResponseEntity.ok(dto);
    }

    @PutMapping
    public ResponseEntity<ClinicalRecordDTO> actualizar(
            @Valid @RequestBody ClinicalRecordDTO dto) {

        // 1. Verificar que la historia clínica exista
        Optional<ClinicalRecord> existente = crS.listId(dto.getIdClinicalRecord());

        if (existente.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe una historia clínica con el id: " + dto.getIdClinicalRecord()
            );
        }

        // 2. Verificar que el perfil de salud exista
        Optional<HealthProfile> hp = hpS.listId(dto.getIdHealthProfile());

        if (hp.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe un perfil de salud con el id: " + dto.getIdHealthProfile()
            );
        }

        // 3. Verificar que la condición médica exista
        Optional<MedicalCondition> mc = mcS.listId(dto.getIdCondition());

        if (mc.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No existe la condición médica con el id: " + dto.getIdCondition()
            );
        }

        // 4. Obtener el cultivo existente
        ClinicalRecord clinicalRecord= existente.get();

        clinicalRecord.setRecordType(dto.getRecordType());
        clinicalRecord.setTitle(dto.getTitle());
        clinicalRecord.setDescription(dto.getDescription());
        clinicalRecord.setEventDate(dto.getEventDate());
        clinicalRecord.setProfessionalName(dto.getProfessionalName());


        // 5. Asignar el perfil de salud y condición médica existente
        clinicalRecord.setHealthProfile(hp.get());
        clinicalRecord.setMedicalCondition(mc.get());

        // 6. Guardar
        crS.update(clinicalRecord);

        // 7. Convertir a DTO
        ClinicalRecordDTO responseDTO =
                modelMapper.map(clinicalRecord, ClinicalRecordDTO.class);

        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        // 1. Verificar que el registro clínico exista
        ClinicalRecord cr = crS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe un registro clínico con el id: " + id
                        )
                );

        // 2. Guardar la actualización
        crS.update(cr);

        // 3. Responder
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ClinicalRecordDTO>> buscarPorTipo(
            @PathVariable String tipo) {

        List<ClinicalRecordDTO> lista = crS.obtenerPorTipo(tipo)
                .stream()
                .map(c -> modelMapper.map(c, ClinicalRecordDTO.class))
                .toList();

        return ResponseEntity.ok(lista);
    }

    @GetMapping("/reporte/condiciones")
    public ResponseEntity<List<CountMedicalConditionDTO>> cantidadPorCondicion() {

        List<CountMedicalConditionDTO> lista = crS.cantidadPorCondicionMedica()
                .stream()
                .map(resultado -> {
                    CountMedicalConditionDTO dto = new CountMedicalConditionDTO();
                    dto.setConditionName((String) resultado[0]);
                    dto.setTotalRecords(((Number) resultado[1]).intValue());
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(lista);
    }
}

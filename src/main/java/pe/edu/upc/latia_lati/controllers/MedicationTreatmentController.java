package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.latia_lati.dtos.MedicationTreatmentDTO;
import pe.edu.upc.latia_lati.entities.ClinicalRecord;
import pe.edu.upc.latia_lati.entities.Medications;
import pe.edu.upc.latia_lati.entities.MedicationTreatment;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IClinicalRecordService;
import pe.edu.upc.latia_lati.serviceinterfaces.IMedicationTreatmentService;
import pe.edu.upc.latia_lati.serviceinterfaces.IMedicationsService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medicationtreatments")
public class MedicationTreatmentController {
    private final IMedicationTreatmentService treatmentService;
    private final IClinicalRecordService clinicalRecordService;
    private final IMedicationsService medicationsService;
    private final ModelMapper modelMapper;

    public MedicationTreatmentController(
            IMedicationTreatmentService treatmentService,
            IClinicalRecordService clinicalRecordService,
            IMedicationsService medicationsService,
            ModelMapper modelMapper) {
        this.treatmentService = treatmentService;
        this.clinicalRecordService = clinicalRecordService;
        this.medicationsService = medicationsService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<MedicationTreatmentDTO>> listar() {
        List<MedicationTreatmentDTO> treatments = treatmentService.list()
                .stream()
                .map(this::convertirDTO)
                .toList();
        return ResponseEntity.ok(treatments);
    }

    @PostMapping
    public ResponseEntity<MedicationTreatmentDTO> registrar(
            @Valid @RequestBody MedicationTreatmentDTO dto) {
        MedicationTreatment treatment = modelMapper.map(dto, MedicationTreatment.class);
        treatment.setId(null);
        treatment.setClinicalRecord(buscarHistoriaClinica(dto.getIdClinicalRecord()));
        treatment.setMedication(buscarMedicamento(dto.getIdMedication()));
        treatmentService.insert(treatment);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(treatment.getId())
                .toUri();
        return ResponseEntity.created(location).body(convertirDTO(treatment));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationTreatmentDTO> buscarPorId(@PathVariable Long id) {
        MedicationTreatment treatment = treatmentService.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un tratamiento de medicamento con el id: " + id));
        return ResponseEntity.ok(convertirDTO(treatment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationTreatmentDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MedicationTreatmentDTO dto) {
        MedicationTreatment treatment = treatmentService.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un tratamiento de medicamento con el id: " + id));

        treatment.setClinicalRecord(buscarHistoriaClinica(dto.getIdClinicalRecord()));
        treatment.setMedication(buscarMedicamento(dto.getIdMedication()));
        treatment.setDose(dto.getDose());
        treatment.setAdministrationRoute(dto.getAdministrationRoute());
        treatment.setStartDate(dto.getStartDate());
        treatment.setEndDate(dto.getEndDate());
        treatmentService.update(treatment);
        return ResponseEntity.ok(convertirDTO(treatment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        MedicationTreatment treatment = treatmentService.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un tratamiento de medicamento con el id: " + id));
        treatmentService.delete(treatment.getId());
        return ResponseEntity.noContent().build();
    }

    private ClinicalRecord buscarHistoriaClinica(Long id) {
        return clinicalRecordService.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una historia clínica con el id: " + id));
    }

    private Medications buscarMedicamento(Long id) {
        return medicationsService.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un medicamento con el id: " + id));
    }

    private MedicationTreatmentDTO convertirDTO(MedicationTreatment treatment) {
        MedicationTreatmentDTO dto = modelMapper.map(treatment, MedicationTreatmentDTO.class);
        dto.setIdMedicationTreatment(treatment.getId());
        dto.setIdClinicalRecord(treatment.getClinicalRecord().getIdClinicalRecord());
        dto.setIdMedication(treatment.getMedication().getIdMedications());
        return dto;
    }
}

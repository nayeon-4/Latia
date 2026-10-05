package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.latia_lati.dtos.ExamResultDTO;
import pe.edu.upc.latia_lati.entities.ExamResult;
import pe.edu.upc.latia_lati.entities.ClinicalRecord;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IClinicalRecordService;
import pe.edu.upc.latia_lati.serviceinterfaces.IExamResultService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/examresults")
public class ExamResultController {
    private final IExamResultService examResultService;
    private final IClinicalRecordService clinicalRecordService;
    private final ModelMapper modelMapper;

    public ExamResultController(
            IExamResultService examResultService,
            IClinicalRecordService clinicalRecordService,
            ModelMapper modelMapper) {
        this.examResultService = examResultService;
        this.clinicalRecordService = clinicalRecordService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<ExamResultDTO>> listar() {
        List<ExamResultDTO> results = examResultService.list()
                .stream()
                .map(this::convertirDTO)
                .toList();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/registro-clinico/{clinicalRecordId}")
    public ResponseEntity<List<ExamResultDTO>> listarPorRegistroClinico(
            @PathVariable Long clinicalRecordId) {
        List<ExamResultDTO> results = examResultService.listarPorHistoriaClinica(clinicalRecordId)
                .stream()
                .map(this::convertirDTO)
                .toList();
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public ResponseEntity<ExamResultDTO> registrar(@Valid @RequestBody ExamResultDTO dto) {
        ClinicalRecord clinicalRecord = buscarHistoriaClinica(dto.getIdClinicalRecord());
        ExamResult examResult = modelMapper.map(dto, ExamResult.class);
        examResult.setId(null);
        examResult.setClinicalRecord(clinicalRecord);
        examResultService.insert(examResult);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(examResult.getId())
                .toUri();
        return ResponseEntity.created(location).body(convertirDTO(examResult));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExamResultDTO> buscarPorId(@PathVariable Long id) {
        ExamResult examResult = examResultService.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un resultado de examen con el id: " + id));
        return ResponseEntity.ok(convertirDTO(examResult));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExamResultDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ExamResultDTO dto) {
        ExamResult examResult = examResultService.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un resultado de examen con el id: " + id));

        examResult.setClinicalRecord(buscarHistoriaClinica(dto.getIdClinicalRecord()));
        examResult.setParameterName(dto.getParameterName());
        examResult.setResultValue(dto.getResultValue());
        examResult.setUnit(dto.getUnit());
        examResultService.update(examResult);
        return ResponseEntity.ok(convertirDTO(examResult));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        ExamResult examResult = examResultService.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un resultado de examen con el id: " + id));
        examResultService.delete(examResult.getId());
        return ResponseEntity.noContent().build();
    }

    private ClinicalRecord buscarHistoriaClinica(Long id) {
        return clinicalRecordService.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe una historia clínica con el id: " + id));
    }

    private ExamResultDTO convertirDTO(ExamResult examResult) {
        ExamResultDTO dto = modelMapper.map(examResult, ExamResultDTO.class);
        dto.setIdExamResult(examResult.getId());
        dto.setIdClinicalRecord(examResult.getClinicalRecord().getIdClinicalRecord());
        return dto;
    }
}

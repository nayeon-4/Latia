package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.edu.upc.latia_lati.dtos.MedicationScheduleDTO;
import pe.edu.upc.latia_lati.entities.MedicationSchedule;
import pe.edu.upc.latia_lati.entities.MedicationTreatment;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IMedicationScheduleService;
import pe.edu.upc.latia_lati.serviceinterfaces.IMedicationTreatmentService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/medicationschedules")
public class MedicationScheduleController {
    private final IMedicationScheduleService scheduleService;
    private final IMedicationTreatmentService treatmentService;
    private final ModelMapper modelMapper;

    public MedicationScheduleController(
            IMedicationScheduleService scheduleService,
            IMedicationTreatmentService treatmentService,
            ModelMapper modelMapper) {
        this.scheduleService = scheduleService;
        this.treatmentService = treatmentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<MedicationScheduleDTO>> listar() {
        List<MedicationScheduleDTO> schedules = scheduleService.list()
                .stream()
                .map(this::convertirDTO)
                .toList();
        return ResponseEntity.ok(schedules);
    }

    @GetMapping("/tratamiento/{treatmentId}")
    public ResponseEntity<List<MedicationScheduleDTO>> listarPorTratamiento(
            @PathVariable Long treatmentId) {
        List<MedicationScheduleDTO> schedules = scheduleService.listarPorTratamiento(treatmentId)
                .stream()
                .map(this::convertirDTO)
                .toList();
        return ResponseEntity.ok(schedules);
    }

    @PostMapping
    public ResponseEntity<MedicationScheduleDTO> registrar(
            @Valid @RequestBody MedicationScheduleDTO dto) {
        MedicationSchedule schedule = modelMapper.map(dto, MedicationSchedule.class);
        schedule.setId(null);
        schedule.setTreatment(buscarTratamiento(dto.getIdTreatment()));
        scheduleService.insert(schedule);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(schedule.getId())
                .toUri();
        return ResponseEntity.created(location).body(convertirDTO(schedule));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationScheduleDTO> buscarPorId(@PathVariable Long id) {
        MedicationSchedule schedule = scheduleService.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un horario de medicamento con el id: " + id));
        return ResponseEntity.ok(convertirDTO(schedule));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationScheduleDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MedicationScheduleDTO dto) {
        MedicationSchedule schedule = scheduleService.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un horario de medicamento con el id: " + id));

        schedule.setTreatment(buscarTratamiento(dto.getIdTreatment()));
        schedule.setReminderTime(dto.getReminderTime());
        schedule.setIntervalHours(dto.getIntervalHours());
        scheduleService.update(schedule);
        return ResponseEntity.ok(convertirDTO(schedule));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        MedicationSchedule schedule = scheduleService.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un horario de medicamento con el id: " + id));
        scheduleService.delete(schedule.getId());
        return ResponseEntity.noContent().build();
    }

    private MedicationTreatment buscarTratamiento(Long id) {
        return treatmentService.listId(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe un tratamiento de medicamento con el id: " + id));
    }

    private MedicationScheduleDTO convertirDTO(MedicationSchedule schedule) {
        MedicationScheduleDTO dto = modelMapper.map(schedule, MedicationScheduleDTO.class);
        dto.setIdMedicationSchedule(schedule.getId());
        dto.setIdTreatment(schedule.getTreatment().getId());
        return dto;
    }
}

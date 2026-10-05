package pe.edu.upc.latia_lati.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class MedicationScheduleDTO {
    private Long idMedicationSchedule;

    @NotNull(message = "El ID del tratamiento es obligatorio")
    @Positive(message = "El ID del tratamiento debe ser positivo")
    private Long idTreatment;

    @NotNull(message = "La fecha y hora del recordatorio son obligatorias")
    private LocalDateTime reminderTime;

    @NotNull(message = "El intervalo en horas es obligatorio")
    @Positive(message = "El intervalo debe ser positivo")
    private Integer intervalHours;

    public Long getIdMedicationSchedule() {
        return idMedicationSchedule;
    }

    public void setIdMedicationSchedule(Long idMedicationSchedule) {
        this.idMedicationSchedule = idMedicationSchedule;
    }

    public Long getIdTreatment() {
        return idTreatment;
    }

    public void setIdTreatment(Long idTreatment) {
        this.idTreatment = idTreatment;
    }

    public LocalDateTime getReminderTime() {
        return reminderTime;
    }

    public void setReminderTime(LocalDateTime reminderTime) {
        this.reminderTime = reminderTime;
    }

    public Integer getIntervalHours() {
        return intervalHours;
    }

    public void setIntervalHours(Integer intervalHours) {
        this.intervalHours = intervalHours;
    }
}

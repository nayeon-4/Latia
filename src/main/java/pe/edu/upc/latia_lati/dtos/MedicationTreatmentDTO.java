package pe.edu.upc.latia_lati.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class MedicationTreatmentDTO {
    private Long idMedicationTreatment;

    @NotNull(message = "El ID de la historia clínica es obligatorio")
    @Positive(message = "El ID de la historia clínica debe ser positivo")
    private Long idClinicalRecord;

    @NotNull(message = "El ID del medicamento es obligatorio")
    @Positive(message = "El ID del medicamento debe ser positivo")
    private Long idMedication;

    @NotBlank(message = "La dosis es obligatoria")
    @Size(max = 100, message = "La dosis no puede superar los 100 caracteres")
    private String dose;

    @NotBlank(message = "La vía de administración es obligatoria")
    @Size(max = 80, message = "La vía de administración no puede superar los 80 caracteres")
    private String administrationRoute;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;

    private LocalDate endDate;

    public Long getIdMedicationTreatment() {
        return idMedicationTreatment;
    }

    public void setIdMedicationTreatment(Long idMedicationTreatment) {
        this.idMedicationTreatment = idMedicationTreatment;
    }

    public Long getIdClinicalRecord() {
        return idClinicalRecord;
    }

    public void setIdClinicalRecord(Long idClinicalRecord) {
        this.idClinicalRecord = idClinicalRecord;
    }

    public Long getIdMedication() {
        return idMedication;
    }

    public void setIdMedication(Long idMedication) {
        this.idMedication = idMedication;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getAdministrationRoute() {
        return administrationRoute;
    }

    public void setAdministrationRoute(String administrationRoute) {
        this.administrationRoute = administrationRoute;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}

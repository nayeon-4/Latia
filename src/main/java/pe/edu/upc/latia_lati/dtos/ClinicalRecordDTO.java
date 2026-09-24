package pe.edu.upc.latia_lati.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.sql.Time;
import java.time.OffsetDateTime;
import java.util.Date;

public class ClinicalRecordDTO {
    private Long idClinicalRecord;

    private Long idHealthProfile;

    @NotBlank(message = "El tipo de historia médica es obligatorio")
    private String recordType;

    @NotBlank(message = "El título de la historia médica es obligatorio")
    private String title;

    @NotBlank(message = "La descripción de la historia médica es obligatoria")
    private String description;

    @NotNull(message = "La fecha del evento es obligatoria")
    private Date eventDate;

    @NotBlank(message = "El nombre del profesional es obligatorio")
    private String professionalName;

    @NotNull(message = "El ID de la condición es obligatorio")
    @Positive(message = "El ID de la condición debe ser un número positivo")
    private Long idCondition;

    public Long getIdClinicalRecord() {
        return idClinicalRecord;
    }

    public void setIdClinicalRecord(Long idClinicalRecord) {
        this.idClinicalRecord = idClinicalRecord;
    }

    public Long getIdHealthProfile() {
        return idHealthProfile;
    }

    public void setIdHealthProfile(Long idHealthProfile) {
        this.idHealthProfile = idHealthProfile;
    }

    public String getRecordType() {
        return recordType;
    }

    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getProfessionalName() {
        return professionalName;
    }

    public void setProfessionalName(String professionalName) {
        this.professionalName = professionalName;
    }

    public Long getIdCondition() {
        return idCondition;
    }

    public void setIdCondition(Long idCondition) {
        this.idCondition = idCondition;
    }
}

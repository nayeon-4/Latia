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

    @NotNull(message = "La hora del evento es obligatoria")
    private Time eventTime;

    @NotBlank(message = "Las notas son obligatorias")
    private String notes;

    @NotBlank(message = "El nombre del profesional es obligatorio")
    private String professionalName;

    @NotBlank(message = "El centro o centro médico es obligatorio")
    private String facilityLabel;

    @NotBlank(message = "El tipo de examen es obligatorio")
    private String examType;

    @NotNull(message = "La fecha de vencimiento de la receta es obligatoria")
    private Date prescriptionExpiresOn;

    @NotBlank(message = "Las instrucciones generales son obligatorias")
    private String generalInstructions;

    @NotBlank(message = "La sustancia es obligatoria")
    private String substance;

    @NotBlank(message = "La reacción es obligatoria")
    private String reaction;

    @NotBlank(message = "La severidad de la alergia es obligatoria")
    private String allergySeverity;

    @NotNull(message = "Debe indicar si la alergia está confirmada")
    private Boolean allergyConfirmed;

    @NotNull(message = "Debe indicar si la alergia está activa")
    private Boolean allergyCurrent;

    @NotBlank(message = "La especialidad es obligatoria")
    private String speciality;

    @NotBlank(message = "La ubicación de la cita es obligatoria")
    private String appointmentLocation;

    @NotNull(message = "La duración en minutos es obligatoria")
    @Positive(message = "La duración debe ser un número positivo")
    private int durationMinutes;

    @NotBlank(message = "El estado de la cita es obligatorio")
    private String appointmentStatus;

    @NotNull(message = "El ID de la condición es obligatorio")
    @Positive(message = "El ID de la condición debe ser un número positivo")
    private Long idCondition;

    @NotNull(message = "La edad aproximada de diagnóstico es obligatoria")
    @PositiveOrZero(message = "La edad aproximada debe ser mayor o igual a 0")
    private Short approximateDiagnosisAge;

    @NotBlank(message = "El estado de la condición es obligatorio")
    private String conditionStatus;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private OffsetDateTime deletedAt;

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

    public Time getEventTime() {
        return eventTime;
    }

    public void setEventTime(Time eventTime) {
        this.eventTime = eventTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String text) {
        this.notes = text;
    }

    public String getProfessionalName() {
        return professionalName;
    }

    public void setProfessionalName(String professionalName) {
        this.professionalName = professionalName;
    }

    public String getFacilityLabel() {
        return facilityLabel;
    }

    public void setFacilityLabel(String facilityLabel) {
        this.facilityLabel = facilityLabel;
    }

    public String getExamType() {
        return examType;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public Date getPrescriptionExpiresOn() {
        return prescriptionExpiresOn;
    }

    public void setPrescriptionExpiresOn(Date prescriptionExpiresOn) {
        this.prescriptionExpiresOn = prescriptionExpiresOn;
    }

    public String getGeneralInstructions() {
        return generalInstructions;
    }

    public void setGeneralInstructions(String generalInstructions) {
        this.generalInstructions = generalInstructions;
    }

    public String getSubstance() {
        return substance;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public String getReaction() {
        return reaction;
    }

    public void setReaction(String reaction) {
        this.reaction = reaction;
    }

    public String getAllergySeverity() {
        return allergySeverity;
    }

    public void setAllergySeverity(String allergySeverity) {
        this.allergySeverity = allergySeverity;
    }

    public Boolean getAllergyConfirmed() {
        return allergyConfirmed;
    }

    public void setAllergyConfirmed(Boolean allergyConfirmed) {
        this.allergyConfirmed = allergyConfirmed;
    }

    public Boolean getAllergyCurrent() {
        return allergyCurrent;
    }

    public void setAllergyCurrent(Boolean allergyCurrent) {
        this.allergyCurrent = allergyCurrent;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getAppointmentLocation() {
        return appointmentLocation;
    }

    public void setAppointmentLocation(String appointmentLocation) {
        this.appointmentLocation = appointmentLocation;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public String getAppointmentStatus() {
        return appointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        this.appointmentStatus = appointmentStatus;
    }

    public Long getIdCondition() {
        return idCondition;
    }

    public void setIdCondition(Long idCondition) {
        this.idCondition = idCondition;
    }

    public Short getApproximateDiagnosisAge() {
        return approximateDiagnosisAge;
    }

    public void setApproximateDiagnosisAge(Short approximateDiagnosisAge) {
        this.approximateDiagnosisAge = approximateDiagnosisAge;
    }

    public String getConditionStatus() {
        return conditionStatus;
    }

    public void setConditionStatus(String conditionStatus) {
        this.conditionStatus = conditionStatus;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(OffsetDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}

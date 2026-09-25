package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Time;
import java.time.OffsetDateTime;
import java.util.Date;

@Entity
@Table(name = "clinical_records")
public class ClinicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idClinicalRecord;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idHealthProfile", nullable = false)
    private HealthProfile healthProfile;

    @Column(name = "recordType", nullable = false, length = 20)
    private String recordType;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "eventDate", nullable = false)
    private Date eventDate;

    @Column(name = "eventTime", nullable = false)
    private Time eventTime;

    @Column(name = "notes", nullable = false)
    private String notes;

    @Column(name = "professionalName", nullable = false, length = 150)
    private String professionalName;

    @Column(name = "facilityLabel", nullable = false, length = 180)
    private String facilityLabel;

    @Column(name = "examType", nullable = false, length = 100)
    private String examType;

    @Column(name = "prescriptionExpiresOn", nullable = false)
    private Date prescriptionExpiresOn;

    @Column(name = "generalInstructions", nullable = false)
    private String generalInstructions;

    @Column(name = "substance", nullable = false, length = 150)
    private String substance;

    @Column(name = "reaction", nullable = false)
    private String reaction;

    @Column(name = "allergySeverity", nullable = false, length = 20)
    private String allergySeverity;

    @Column(name = "allergyConfirmed", nullable = false)
    private Boolean allergyConfirmed;

    @Column(name = "allergyCurrent", nullable = false)
    private Boolean allergyCurrent;

    @Column(name = "speciality", nullable = false, length = 120)
    private String speciality;

    @Column(name = "appointmentLocation", nullable = false, length = 255)
    private String appointmentLocation;

    @Column(name = "durationMinutes", nullable = false)
    private int durationMinutes;

    @Column(name = "appointmentStatus", nullable = false, length = 30)
    private String appointmentStatus;

    @ManyToOne
    @JoinColumn(name = "idCondition", nullable = false)
    private MedicalCondition medicalCondition;

    @Column(name = "approximateDiagnosisAge", nullable = false)
    private Short approximateDiagnosisAge;

    @Column(name = "conditionStatus", nullable = false, length = 120)
    private String conditionStatus;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt", nullable = true)
    private OffsetDateTime updatedAt;

    @Column(name = "deletedAt", nullable = true)
    private OffsetDateTime deletedAt;

    public ClinicalRecord() {
    }

    public ClinicalRecord(Long idClinicalRecord, HealthProfile healthProfile, String recordType, String title, String description, Date eventDate, Time eventTime, String notes, String professionalName, String facilityLabel, String examType, Date prescriptionExpiresOn, String generalInstructions, String substance, String reaction, String allergySeverity, Boolean allergyConfirmed, Boolean allergyCurrent, String speciality, String appointmentLocation, int durationMinutes, String appointmentStatus, MedicalCondition medicalCondition, Short approximateDiagnosisAge, String conditionStatus, OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime deletedAt) {
        this.idClinicalRecord = idClinicalRecord;
        this.healthProfile = healthProfile;
        this.recordType = recordType;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.notes = notes;
        this.professionalName = professionalName;
        this.facilityLabel = facilityLabel;
        this.examType = examType;
        this.prescriptionExpiresOn = prescriptionExpiresOn;
        this.generalInstructions = generalInstructions;
        this.substance = substance;
        this.reaction = reaction;
        this.allergySeverity = allergySeverity;
        this.allergyConfirmed = allergyConfirmed;
        this.allergyCurrent = allergyCurrent;
        this.speciality = speciality;
        this.appointmentLocation = appointmentLocation;
        this.durationMinutes = durationMinutes;
        this.appointmentStatus = appointmentStatus;
        this.medicalCondition = medicalCondition;
        this.approximateDiagnosisAge = approximateDiagnosisAge;
        this.conditionStatus = conditionStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public Long getIdClinicalRecord() {
        return idClinicalRecord;
    }

    public void setIdClinicalRecord(Long idClinicalRecord) {
        this.idClinicalRecord = idClinicalRecord;
    }

    public HealthProfile getHealthProfile() {
        return healthProfile;
    }

    public void setHealthProfile(HealthProfile healthProfile) {
        this.healthProfile = healthProfile;
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

    public void setNotes(String notes) {
        this.notes = notes;
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

    public MedicalCondition getMedicalCondition() {
        return medicalCondition;
    }

    public void setMedicalCondition(MedicalCondition medicalCondition) {
        this.medicalCondition = medicalCondition;
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

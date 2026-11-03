package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Column(name = "professionalName", nullable = false, length = 150)
    private String professionalName;

    @ManyToOne
    @JoinColumn(name = "idCondition", nullable = false)
    private MedicalCondition medicalCondition;

    @OneToMany(mappedBy = "clinicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamResult> examResults = new ArrayList<>();

    // 2. Tratamientos con medicamentos recetados en este registro clínico
    @OneToMany(mappedBy = "clinicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicationTreatment> medicationTreatments = new ArrayList<>();

    // 3. Documentos médicos asociados mediante la tabla intermedia
    @OneToMany(mappedBy = "clinicalRecord", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClinicalRecordDocument> clinicalRecordDocuments = new ArrayList<>();

    public ClinicalRecord() {
    }

    public ClinicalRecord(Long idClinicalRecord, HealthProfile healthProfile, String recordType, String title, String description, Date eventDate, String professionalName, MedicalCondition medicalCondition) {
        this.idClinicalRecord = idClinicalRecord;
        this.healthProfile = healthProfile;
        this.recordType = recordType;
        this.title = title;
        this.description = description;
        this.eventDate = eventDate;
        this.professionalName = professionalName;
        this.medicalCondition = medicalCondition;
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

    public String getProfessionalName() {
        return professionalName;
    }

    public void setProfessionalName(String professionalName) {
        this.professionalName = professionalName;
    }

    public MedicalCondition getMedicalCondition() {
        return medicalCondition;
    }

    public void setMedicalCondition(MedicalCondition medicalCondition) {
        this.medicalCondition = medicalCondition;
    }

    public List<ExamResult> getExamResults() {
        return examResults;
    }

    public void setExamResults(List<ExamResult> examResults) {
        this.examResults = examResults;
    }

    public List<ClinicalRecordDocument> getClinicalRecordDocuments() {
        return clinicalRecordDocuments;
    }

    public void setClinicalRecordDocuments(List<ClinicalRecordDocument> clinicalRecordDocuments) {
        this.clinicalRecordDocuments = clinicalRecordDocuments;
    }

    public List<MedicationTreatment> getMedicationTreatments() {
        return medicationTreatments;
    }

    public void setMedicationTreatments(List<MedicationTreatment> medicationTreatments) {
        this.medicationTreatments = medicationTreatments;
    }
}
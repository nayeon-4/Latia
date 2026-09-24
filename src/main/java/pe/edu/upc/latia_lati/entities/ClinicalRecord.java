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

    @Column(name = "professionalName", nullable = false, length = 150)
    private String professionalName;

    @ManyToOne
    @JoinColumn(name = "idCondition", nullable = false)
    private MedicalCondition medicalCondition;

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
}

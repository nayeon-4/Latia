package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "medical_documents")
public class MedicalDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "health_profile_id", nullable = false)
    private HealthProfile healthProfile;

    @Column(name = "document_type", nullable = false, length = 80)
    private String documentType;

    @Column(name = "title", nullable = false, length = 150)
    private String title;

    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;

    @Column(name = "document_date", nullable = false)
    private LocalDate documentDate;

    @JsonIgnore
    @OneToMany(mappedBy = "medicalDocument")
    private List<ClinicalRecordDocument> clinicalRecordDocuments;

    public MedicalDocument() {
    }

    public MedicalDocument(Long id, HealthProfile healthProfile, String documentType, String title,
                           String fileUrl, LocalDate documentDate) {
        this.id = id;
        this.healthProfile = healthProfile;
        this.documentType = documentType;
        this.title = title;
        this.fileUrl = fileUrl;
        this.documentDate = documentDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public HealthProfile getHealthProfile() { return healthProfile; }
    public void setHealthProfile(HealthProfile healthProfile) { this.healthProfile = healthProfile; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String fileUrl) { this.fileUrl = fileUrl; }
    public LocalDate getDocumentDate() { return documentDate; }
    public void setDocumentDate(LocalDate documentDate) { this.documentDate = documentDate; }
    public List<ClinicalRecordDocument> getClinicalRecordDocuments() { return clinicalRecordDocuments; }
    public void setClinicalRecordDocuments(List<ClinicalRecordDocument> clinicalRecordDocuments) {
        this.clinicalRecordDocuments = clinicalRecordDocuments;
    }
}

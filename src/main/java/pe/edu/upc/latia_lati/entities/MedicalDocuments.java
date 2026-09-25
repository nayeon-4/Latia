package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
@Entity
@Table(name = "medical_documents")
public class MedicalDocuments {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long idMedicalDocuments;
    @Column(name = "documentType", length = 50, nullable = false)
    private String documentType;
    @Column(name = "title", length = 100, nullable = false)
    private String title;
    @Column(name = "fileURL", length = 200, nullable = false)
    private String fileURL;
    @Column(name = "documentDate")
    private LocalDate documentDate;

    @ManyToOne
    @JoinColumn(name = "idHealthProfile", nullable = false)
    private HealthProfile healthProfile;

    public MedicalDocuments() {

    }

    public MedicalDocuments(Long idMedicalDocuments, String documentType, String title, String fileURL, LocalDate documentDate, HealthProfile healthProfile) {
        this.idMedicalDocuments = idMedicalDocuments;
        this.documentType = documentType;
        this.title = title;
        this.fileURL = fileURL;
        this.documentDate = documentDate;
        this.healthProfile = healthProfile;
    }

    public Long getIdMedicalDocuments() {
        return idMedicalDocuments;
    }

    public void setIdMedicalDocuments(Long idMedicalDocuments) {
        this.idMedicalDocuments = idMedicalDocuments;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileURL() {
        return fileURL;
    }

    public void setFileURL(String fileURL) {
        this.fileURL = fileURL;
    }

    public LocalDate getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(LocalDate documentDate) {
        this.documentDate = documentDate;
    }

    public HealthProfile getHealthProfile() {
        return healthProfile;
    }

    public void setHealthProfile(HealthProfile healthProfile) {
        this.healthProfile = healthProfile;
    }
}

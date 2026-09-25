package pe.edu.upc.latia_lati.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class MedicalDocumentsDTO {
    private Long idMedicalDocuments;
    @NotBlank(message = "El tipo de documento es obligatorio")
    private String documentType;
    @NotBlank(message = "El título es obligatorio")
    private String title;
    @NotBlank(message = "La URL del archivo es obligatoria")
    private String fileURL;
    @NotNull(message = "La fecha del documento es obligatoria")
    private LocalDate documentDate;
    @NotNull(message = "El id del perfil de salud es obligatorio")
    private Long idHealthProfile;

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

    public Long getIdHealthProfile() {
        return idHealthProfile;
    }

    public void setIdHealthProfile(Long idHealthProfile) {
        this.idHealthProfile = idHealthProfile;
    }
}

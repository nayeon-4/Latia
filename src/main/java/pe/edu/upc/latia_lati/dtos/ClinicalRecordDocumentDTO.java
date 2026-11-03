package pe.edu.upc.latia_lati.dtos;

public class ClinicalRecordDocumentDTO {
    private Long id;
    private Long clinicalRecordId;
    private Long medicalDocumentId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClinicalRecordId() {
        return clinicalRecordId;
    }

    public void setClinicalRecordId(Long clinicalRecordId) {
        this.clinicalRecordId = clinicalRecordId;
    }

    public Long getMedicalDocumentId() {
        return medicalDocumentId;
    }

    public void setMedicalDocumentId(Long medicalDocumentId) {
        this.medicalDocumentId = medicalDocumentId;
    }
}


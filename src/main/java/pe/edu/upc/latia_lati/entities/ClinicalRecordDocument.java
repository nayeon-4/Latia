package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "clinical_record_documents")
public class ClinicalRecordDocument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "clinical_record_id", nullable = false)
    private ClinicalRecord clinicalRecord;

    @ManyToOne(optional = false)
    @JoinColumn(name = "medical_document_id", nullable = false)
    private MedicalDocuments medicalDocument;

    public ClinicalRecordDocument() {
    }

    public ClinicalRecordDocument(Long id, ClinicalRecord clinicalRecord, MedicalDocuments medicalDocument) {
        this.id = id;
        this.clinicalRecord = clinicalRecord;
        this.medicalDocument = medicalDocument;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ClinicalRecord getClinicalRecord() { return clinicalRecord; }
    public void setClinicalRecord(ClinicalRecord clinicalRecord) { this.clinicalRecord = clinicalRecord; }
    public MedicalDocuments getMedicalDocument() { return medicalDocument; }
    public void setMedicalDocument(MedicalDocuments medicalDocument) { this.medicalDocument = medicalDocument; }
}

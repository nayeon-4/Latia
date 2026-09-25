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
    private MedicalDocument medicalDocument;

    public ClinicalRecordDocument() {
    }

    public ClinicalRecordDocument(Long id, ClinicalRecord clinicalRecord, MedicalDocument medicalDocument) {
        this.id = id;
        this.clinicalRecord = clinicalRecord;
        this.medicalDocument = medicalDocument;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ClinicalRecord getClinicalRecord() { return clinicalRecord; }
    public void setClinicalRecord(ClinicalRecord clinicalRecord) { this.clinicalRecord = clinicalRecord; }
    public MedicalDocument getMedicalDocument() { return medicalDocument; }
    public void setMedicalDocument(MedicalDocument medicalDocument) { this.medicalDocument = medicalDocument; }
}

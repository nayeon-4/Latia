package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medical_conditions")
public class MedicalCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMedicalCondition;

    @Column(name = "nameMedicalCondition", nullable = false, length = 150)
    private String nameMedicalCondition;

    @Column(name = "descriptionMedicalCondition", nullable = false, length = 255)
    private String descriptionMedicalCondition;

    @OneToMany(mappedBy = "medicalCondition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClinicalRecord> clinicalRecords = new ArrayList<>();


    public MedicalCondition() {
    }

    public MedicalCondition(Long idMedicalCondition, String nameMedicalCondition, String descriptionMedicalCondition) {
        this.idMedicalCondition = idMedicalCondition;
        this.nameMedicalCondition = nameMedicalCondition;
        this.descriptionMedicalCondition = descriptionMedicalCondition;
    }

    public Long getIdMedicalCondition() {
        return idMedicalCondition;
    }

    public void setIdMedicalCondition(Long idMedicalCondition) {
        this.idMedicalCondition = idMedicalCondition;
    }

    public String getNameMedicalCondition() {
        return nameMedicalCondition;
    }

    public void setNameMedicalCondition(String nameMedicalCondition) {
        this.nameMedicalCondition = nameMedicalCondition;
    }

    public String getDescriptionMedicalCondition() {
        return descriptionMedicalCondition;
    }

    public void setDescriptionMedicalCondition(String descriptionMedicalCondition) {
        this.descriptionMedicalCondition = descriptionMedicalCondition;
    }

    public List<ClinicalRecord> getClinicalRecords() {
        return clinicalRecords;
    }

    public void setClinicalRecords(List<ClinicalRecord> clinicalRecords) {
        this.clinicalRecords = clinicalRecords;
    }
}

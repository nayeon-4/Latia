package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="medications")
public class Medications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMedications;
    @Column(name = "nameMedications", length = 50, nullable = false)
    private String nameMedications;
    @Column(name = "dosageForm", length = 25, nullable = false)
    private String dosageForm;
    @Column(name = "strength", length = 50)
    private String strength;
    @OneToMany(mappedBy = "medication", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicationTreatment> medicationTreatments = new ArrayList<>();
    public Medications() {
    }

    public Medications(Long idMedications, String nameMedications, String dosageForm, String strength) {
        this.idMedications = idMedications;
        this.nameMedications = nameMedications;
        this.dosageForm = dosageForm;
        this.strength = strength;
    }

    public Long getIdMedications() {
        return idMedications;
    }

    public void setIdMedications(Long idMedications) {
        this.idMedications = idMedications;
    }

    public String getNameMedications() {
        return nameMedications;
    }

    public void setNameMedications(String nameMedications) {
        this.nameMedications = nameMedications;
    }

    public String getDosageForm() {
        return dosageForm;
    }

    public void setDosageForm(String dosageForm) {
        this.dosageForm = dosageForm;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public List<MedicationTreatment> getMedicationTreatments() {
        return medicationTreatments;
    }

    public void setMedicationTreatments(List<MedicationTreatment> medicationTreatments) {
        this.medicationTreatments = medicationTreatments;
    }
}

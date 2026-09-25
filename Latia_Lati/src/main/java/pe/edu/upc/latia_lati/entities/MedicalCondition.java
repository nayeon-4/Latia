package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;

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

    @Column(name = "active", nullable = false)
    private Boolean active;

    public MedicalCondition() {
    }

    public MedicalCondition(Long idMedicalCondition, String nameMedicalCondition, String descriptionMedicalCondition, Boolean active) {
        this.idMedicalCondition = idMedicalCondition;
        this.nameMedicalCondition = nameMedicalCondition;
        this.descriptionMedicalCondition = descriptionMedicalCondition;
        this.active = active;
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

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

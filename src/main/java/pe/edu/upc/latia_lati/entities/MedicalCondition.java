package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "medical_conditions")
public class MedicalCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long idMedicalCondition;

    @Column(name = "name", nullable = false, length = 150)
    private String nameMedicalCondition;

    @Column(name = "description", nullable = true, length = 255)
    private String descriptionMedicalCondition;


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
}

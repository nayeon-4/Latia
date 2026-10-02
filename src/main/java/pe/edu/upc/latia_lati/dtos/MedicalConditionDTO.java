package pe.edu.upc.latia_lati.dtos;

import jakarta.validation.constraints.NotBlank;

public class MedicalConditionDTO {
    private Long idMedicalCondition;

    @NotBlank(message = "El nombre de la condición médica es obligatorio")
    private String nameMedicalCondition;

    @NotBlank(message = "La descripción de la condición médica es obligatoria")
    private String descriptionMedicalCondition;

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

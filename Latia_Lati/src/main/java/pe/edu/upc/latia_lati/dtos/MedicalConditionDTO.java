package pe.edu.upc.latia_lati.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class MedicalConditionDTO {
    private Long idMedicalCondition;

    @NotBlank(message = "El nombre de la condición médica es obligatorio")
    private String nameMedicalCondition;

    @NotBlank(message = "La descripción de la condición médica es obligatoria")
    private String descriptionMedicalCondition;

    @NotNull(message = "El estado es obligatorio")
    private Boolean active;

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

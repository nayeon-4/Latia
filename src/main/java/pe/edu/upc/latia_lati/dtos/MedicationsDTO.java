package pe.edu.upc.latia_lati.dtos;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;

public class MedicationsDTO {
    private Long idMedications;
    @NotBlank(message="El nombre es obligatorio")
    private String nameMedications;
    @NotBlank(message="La forma de dosificación es obligatoria")
    private String dosageForm;
    @NotBlank(message="La potencia es obligatoria")
    private String strength;

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
}

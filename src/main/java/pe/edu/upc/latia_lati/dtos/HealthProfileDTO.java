package pe.edu.upc.latia_lati.dtos;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import jakarta.validation.constraints.*;


public class HealthProfileDTO {
    private Long idHealthProfile;

    @NotNull(message = "El ID del usuario propietario es obligatorio")
    @Positive(message = "El ID del usuario propietario debe ser positivo")
    private Long idOwnerUser;

    private Long idHolderUser;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    private LocalDate birthDate;

    @NotBlank(message = "El sexo es obligatorio")
    private String sex;

    @NotBlank(message = "El tipo de sangre es obligatorio")
    private String bloodType;

    @NotBlank(message = "El teléfono es obligatorio")
    private String phone;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean active;

    public Long getIdHealthProfile() {
        return idHealthProfile;
    }

    public void setIdHealthProfile(Long idHealthProfile) {
        this.idHealthProfile = idHealthProfile;
    }

    public Long getIdOwnerUser() {
        return idOwnerUser;
    }

    public void setIdOwnerUser(Long idOwnerUser) {
        this.idOwnerUser = idOwnerUser;
    }

    public Long getIdHolderUser() {
        return idHolderUser;
    }

    public void setIdHolderUser(Long idHolderUser) {
        this.idHolderUser = idHolderUser;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}

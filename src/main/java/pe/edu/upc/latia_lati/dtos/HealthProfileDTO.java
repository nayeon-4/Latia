package pe.edu.upc.latia_lati.dtos;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import jakarta.validation.constraints.*;


public class HealthProfileDTO {
    private Long idHealthProfile;

    private Long idOwnerUser;

    private Long idHolderUser;

    @NotBlank(message = "El nombre es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    private String lastName;

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

    @NotNull(message = "La fecha de creación es obligatoria")
    @PastOrPresent(message = "La fecha de creación no puede ser futura")
    private OffsetDateTime createdAt;

    @PastOrPresent(message = "La fecha de actualización no puede ser futura")
    private OffsetDateTime updatedAt;

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

package pe.edu.upc.latia_lati.dtos;

import java.time.OffsetDateTime;
import jakarta.validation.constraints.*;

public class EmergencyContactDTO {
    private Long idEmergencyContact;

    private Long idHealthProfile;

    @NotBlank(message = "El nombre del contacto de emergencia es obligatorio")
    private String name;

    @NotBlank(message = "El teléfono es obligatorio")
    private String phone;

    @NotBlank(message = "El parentesco es obligatorio")
    private String relationship;

    @NotNull(message = "Debe indicar si es el contacto principal")
    private Boolean primaryContact;

    @NotNull(message = "La fecha de creación es obligatoria")
    @PastOrPresent(message = "La fecha de creación no puede ser futura")
    private OffsetDateTime createdAt;

    @PastOrPresent(message = "La fecha de actualización no puede ser futura")
    private OffsetDateTime updatedAt;

    public Long getIdEmergencyContact() {
        return idEmergencyContact;
    }

    public void setIdEmergencyContact(Long idEmergencyContact) {
        this.idEmergencyContact = idEmergencyContact;
    }

    public Long getIdHealthProfile() {
        return idHealthProfile;
    }

    public void setIdHealthProfile(Long idHealthProfile) {
        this.idHealthProfile = idHealthProfile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public Boolean getPrimaryContact() {
        return primaryContact;
    }

    public void setPrimaryContact(Boolean primaryContact) {
        this.primaryContact = primaryContact;
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

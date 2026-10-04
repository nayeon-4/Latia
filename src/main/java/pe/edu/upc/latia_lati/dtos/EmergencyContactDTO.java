package pe.edu.upc.latia_lati.dtos;

import java.time.OffsetDateTime;
import jakarta.validation.constraints.*;

public class EmergencyContactDTO {
    private Long idEmergencyContact;

    @NotNull(message = "El id del perfil de salud es obligatorio")
    private Long idHealthProfile;

    @NotBlank(message = "El nombre del contacto de emergencia es obligatorio")
    @Size(max = 150, message = "El nombre no puede superar los 150 caracteres")
    private String name;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^\\+?[0-9]( ?[0-9]){6,14}$", message = "El teléfono debe tener entre 7 y 15 dígitos")
    private String phone;

    @NotBlank(message = "El parentesco es obligatorio")
    @Size(max = 80, message = "El parentesco no puede superar los 80 caracteres")
    private String relationship;

    @NotNull(message = "Debe indicar si es el contacto principal")
    private Boolean primaryContact;

    // Las fechas las genera la base de datos (@CreationTimestamp / @UpdateTimestamp),
    // por eso no se validan, solo devuelve la respuesta.
    private OffsetDateTime createdAt;

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
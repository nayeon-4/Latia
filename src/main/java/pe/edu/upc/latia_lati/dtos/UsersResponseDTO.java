package pe.edu.upc.latia_lati.dtos;

import java.time.OffsetDateTime;

import jakarta.validation.constraints.*;

public class UsersResponseDTO {
    private Long idUser;
    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String firstName;

    @NotBlank(message = "El apellido del usuario es obligatorio")
    private String lastName;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    private String username;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe tener un formato válido")
    private String email;

    @NotNull(message = "El estado activo es obligatorio")
    private Boolean active;

    @PastOrPresent(message = "La fecha de creación no puede ser futura")
    private OffsetDateTime createdAt;

    @PastOrPresent(message = "La fecha de actualización no puede ser futura")
    private OffsetDateTime updatedAt;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
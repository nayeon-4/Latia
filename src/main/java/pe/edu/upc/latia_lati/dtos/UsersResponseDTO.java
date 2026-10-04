package pe.edu.upc.latia_lati.dtos;

import java.time.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonAlias;

public class UsersResponseDTO {
    private Long idUser;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private Boolean active;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    public UsersResponseDTO() {
    }

    public UsersResponseDTO(Long idUser, String firstName, String lastName, String username, String email, Boolean active, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.idUser = idUser;
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getIdUser() { return idUser; }
    public void setIdUser(Long idUser) { this.idUser = idUser; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public OffsetDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(OffsetDateTime updatedAt) { this.updatedAt = updatedAt; }

}

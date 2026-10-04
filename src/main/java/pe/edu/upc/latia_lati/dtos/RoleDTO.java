package pe.edu.upc.latia_lati.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RoleDTO {
    private Long id;

    @NotBlank(message = "El rol no debe estar vacío")
    private String rol;

    @NotNull(message = "El id del usuario no debe ser nulo")
    @Positive(message = "El id del usuario debe ser positivo")
    private Long idUser;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}

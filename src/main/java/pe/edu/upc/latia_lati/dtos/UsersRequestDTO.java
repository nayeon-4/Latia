package pe.edu.upc.latia_lati.dtos;

import java.time.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonAlias;

public class UsersRequestDTO {
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre admite hasta 100 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100, message = "El apellido admite hasta 100 caracteres")
    private String lastName;

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Pattern(regexp = "[A-Za-z0-9._-]{3,50}", message = "El username debe tener entre 3 y 50 caracteres: letras, números, punto, guion o guion bajo")
    private String username;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico debe tener un formato válido")
    @Size(max = 150, message = "El correo admite hasta 150 caracteres")
    private String email;

    @JsonAlias("passwordHash")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres; además no debe exceder 72 bytes UTF-8")
    private String password;

    public UsersRequestDTO() {
    }

    public UsersRequestDTO(String firstName, String lastName, String username, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

}

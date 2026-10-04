package pe.edu.upc.latia_lati.dtos;
import jakarta.validation.constraints.*;

/** PUT completo de los datos editables. La contraseña se cambia por separado. */
public class UpdateUserDTO {
    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @NotBlank
    @Pattern(regexp = "[A-Za-z0-9._-]{3,50}")
    private String username;

    @NotBlank
    @Email
    @Size(max = 150)
    private String email;

    public UpdateUserDTO() {
    }

    public UpdateUserDTO(String firstName, String lastName, String username, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
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

}

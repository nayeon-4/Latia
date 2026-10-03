package pe.edu.upc.latia_lati.dtos;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.*;

/** Entrada de alta: ID, roles, estado y fechas siempre los decide el servidor. */
public class UsersRequestDTO {
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

    @JsonAlias("passwordHash")
    @NotBlank
    @Size(min = 8, max = 72)
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

package pe.edu.upc.latia_lati.dtos;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

/** El propietario se deriva del JWT. Si se envía, su ID debe coincidir. */
public class HealthProfileRequestDTO {
    @NotBlank
    @Size(max = 100)
    private String firstName;

    @NotBlank
    @Size(max = 100)
    private String lastName;

    @Positive
    private Long idOwnerUser;

    @Positive
    private Long idHolderUser;

    @NotNull
    @PastOrPresent
    private LocalDate birthDate;

    @NotBlank
    @Size(max = 30)
    private String sex;

    @NotBlank
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Tipo de sangre inválido")
    private String bloodType;

    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "^[+0-9() .-]{5,30}$", message = "Teléfono inválido")
    private String phone;

    @NotNull
    private Boolean active;

    public HealthProfileRequestDTO() {
    }

    public HealthProfileRequestDTO(String firstName, String lastName, Long idOwnerUser, Long idHolderUser, LocalDate birthDate, String sex, String bloodType, String phone, Boolean active) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.idOwnerUser = idOwnerUser;
        this.idHolderUser = idHolderUser;
        this.birthDate = birthDate;
        this.sex = sex;
        this.bloodType = bloodType;
        this.phone = phone;
        this.active = active;
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

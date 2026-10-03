package pe.edu.upc.latia_lati.dtos;
import java.time.LocalDate;
import java.time.OffsetDateTime;
public class HealthProfileDTO {
    private Long idHealthProfile;

    private String firstName;

    private String lastName;

    private Long idOwnerUser;

    private Long idHolderUser;

    private LocalDate birthDate;

    private String sex;

    private String bloodType;

    private String phone;

    private Boolean active;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    public HealthProfileDTO() {
    }

    public HealthProfileDTO(Long idHealthProfile, String firstName, String lastName, Long idOwnerUser, Long idHolderUser, LocalDate birthDate, String sex, String bloodType, String phone, Boolean active, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.idHealthProfile = idHealthProfile;
        this.firstName = firstName;
        this.lastName = lastName;
        this.idOwnerUser = idOwnerUser;
        this.idHolderUser = idHolderUser;
        this.birthDate = birthDate;
        this.sex = sex;
        this.bloodType = bloodType;
        this.phone = phone;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getIdHealthProfile() {
        return idHealthProfile;
    }

    public void setIdHealthProfile(Long idHealthProfile) {
        this.idHealthProfile = idHealthProfile;
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

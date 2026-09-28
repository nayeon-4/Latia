package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "health_profiles")
public class HealthProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idHealthProfile;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idOwnerUser", nullable = false)
    private Users ownerUser;

    @OneToOne(optional = true)
    @JoinColumn(name = "idHolderUser", nullable = true, unique = true)
    private Users holderUser;

    @Column(name = "firstName", nullable = false, length = 100)
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 100)
    private String lastName;

    @Column(name = "birthDate", nullable = true)
    private LocalDate birthDate;

    @Column(name = "sex", nullable = true, length = 30)
    private String sex;

    @Column(name = "bloodType", nullable = true, length = 5)
    private String bloodType;

    @Column(name = "phone", nullable = true, length = 30)
    private String phone;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDate createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt", nullable = true)
    private LocalDate updatedAt;

    public HealthProfile() {

    }

    public Long getIdHealthProfile() {
        return idHealthProfile;
    }

    public void setIdHealthProfile(Long idHealthProfile) {
        this.idHealthProfile = idHealthProfile;
    }

    public Users getOwnerUser() {
        return ownerUser;
    }

    public void setOwnerUser(Users ownerUser) {
        this.ownerUser = ownerUser;
    }

    public Users getHolderUser() {
        return holderUser;
    }

    public void setHolderUser(Users holderUser) {
        this.holderUser = holderUser;
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

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }
}

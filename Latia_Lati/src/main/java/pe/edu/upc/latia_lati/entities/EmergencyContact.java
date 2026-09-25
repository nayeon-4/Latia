package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "emergency_contacts")
public class EmergencyContact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEmergencyContact;

    @ManyToOne(optional = false)
    @JoinColumn(name = "idHealthProfile", nullable = false)
    private HealthProfile healthProfile;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "phone", nullable = false, length = 30)
    private String phone;

    @Column(name = "relationship", nullable = true, length = 80)
    private String relationship;

    @Column(name = "primaryContact", nullable = false)
    private Boolean primaryContact;

    @CreationTimestamp
    @Column(name = "createdAt", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updatedAt", nullable = true)
    private OffsetDateTime updatedAt;

    public EmergencyContact() {

    }

    public EmergencyContact(Long idEmergencyContact, HealthProfile healthProfile, String name, String phone, String relationship, Boolean primaryContact, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.idEmergencyContact = idEmergencyContact;
        this.healthProfile = healthProfile;
        this.name = name;
        this.phone = phone;
        this.relationship = relationship;
        this.primaryContact = primaryContact;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getIdEmergencyContact() {
        return idEmergencyContact;
    }

    public void setIdEmergencyContact(Long idEmergencyContact) {
        this.idEmergencyContact = idEmergencyContact;
    }

    public HealthProfile getHealthProfile() {
        return healthProfile;
    }

    public void setHealthProfile(HealthProfile healthProfile) {
        this.healthProfile = healthProfile;
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

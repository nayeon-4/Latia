package pe.edu.upc.latia_lati.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "family_relationships")
public class FamilyRelationship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "origin_profile_id", nullable = false)
    private HealthProfile originProfile;

    @ManyToOne(optional = false)
    @JoinColumn(name = "relative_profile_id", nullable = false)
    private HealthProfile relativeProfile;

    @Column(name = "relationship_type", nullable = false, length = 80)
    private String relationshipType;

    public FamilyRelationship() {
    }

    public FamilyRelationship(Long id, HealthProfile originProfile, HealthProfile relativeProfile, String relationshipType) {
        this.id = id;
        this.originProfile = originProfile;
        this.relativeProfile = relativeProfile;
        this.relationshipType = relationshipType;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public HealthProfile getOriginProfile() { return originProfile; }
    public void setOriginProfile(HealthProfile originProfile) { this.originProfile = originProfile; }
    public HealthProfile getRelativeProfile() { return relativeProfile; }
    public void setRelativeProfile(HealthProfile relativeProfile) { this.relativeProfile = relativeProfile; }
    public String getRelationshipType() { return relationshipType; }
    public void setRelationshipType(String relationshipType) { this.relationshipType = relationshipType; }
}

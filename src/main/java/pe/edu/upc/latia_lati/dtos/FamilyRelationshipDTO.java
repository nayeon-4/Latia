package pe.edu.upc.latia_lati.dtos;

import jakarta.validation.constraints.*;

public class FamilyRelationshipDTO {
    private Long idFamilyRelationship;

    @NotNull(message = "El id del perfil de origen es obligatorio")
    @Positive(message = "El id del perfil de origen debe ser positivo")
    private Long idOriginProfile;

    @NotNull(message = "El id del perfil del familiar es obligatorio")
    @Positive(message = "El id del perfil del familiar debe ser positivo")
    private Long idRelativeProfile;

    @NotBlank(message = "El tipo de relación es obligatorio")
    @Size(max = 30, message = "El tipo de relación no puede superar los 30 caracteres")
    private String relationshipType;

    public Long getIdFamilyRelationship() {
        return idFamilyRelationship;
    }

    public void setIdFamilyRelationship(Long idFamilyRelationship) {
        this.idFamilyRelationship = idFamilyRelationship;
    }

    public Long getIdOriginProfile() {
        return idOriginProfile;
    }

    public void setIdOriginProfile(Long idOriginProfile) {
        this.idOriginProfile = idOriginProfile;
    }

    public Long getIdRelativeProfile() {
        return idRelativeProfile;
    }

    public void setIdRelativeProfile(Long idRelativeProfile) {
        this.idRelativeProfile = idRelativeProfile;
    }

    public String getRelationshipType() {
        return relationshipType;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }
}
package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.FamilyRelationship;
import pe.edu.upc.latia_lati.entities.HealthProfile;

import java.util.List;

@Repository
public interface IFamilyRelationshipRepository extends JpaRepository<FamilyRelationship, Long> {
    List<FamilyRelationship> findByOriginProfileInOrRelativeProfileIn(
            List<HealthProfile> originProfiles,
            List<HealthProfile> relativeProfiles);
    List<FamilyRelationship> findByOriginProfile_IdHealthProfileOrRelativeProfile_IdHealthProfile(
            Long originProfileId,
            Long relativeProfileId);
}

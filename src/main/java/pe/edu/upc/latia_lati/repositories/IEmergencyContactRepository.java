package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.EmergencyContact;
import pe.edu.upc.latia_lati.entities.HealthProfile;

import java.util.List;

@Repository
public interface IEmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
    List<EmergencyContact> findByHealthProfileIn(List<HealthProfile> healthProfiles);

    // HU68 (simple): contactos marcados como principales
    public List<EmergencyContact> findByPrimaryContactTrue();

    // HU69 (join): ficha de emergencia de cada perfil de salud activo con su contacto principal
    @Query(value = "SELECT hp.id, hp.first_name, hp.last_name, hp.blood_type, " +
            "ec.name, ec.phone, ec.relationship " +
            "FROM health_profiles hp " +
            "INNER JOIN emergency_contacts ec " +
            "ON ec.health_profile_id = hp.id " +
            "WHERE ec.primary_contact = true AND hp.active = true " +
            "ORDER BY hp.last_name, hp.first_name",
            nativeQuery = true)
    public List<Object[]> getEmergencyCards();
}
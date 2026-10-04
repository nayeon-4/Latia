package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.HealthProfile;
import java.util.List;

@Repository
public interface IHealthProfileRepository extends JpaRepository<HealthProfile, Long> {
    List<HealthProfile> findByOwnerUser_IdUserOrderByIdHealthProfileAsc(Long idUser);
    boolean existsByHolderUser_IdUser(Long idUser);
    boolean existsByHolderUser_IdUserAndIdHealthProfileNot(Long idUser, Long idHealthProfile);

    // Consulta simple por atributo; el propietario evita mostrar perfiles ajenos.
    List<HealthProfile> findByOwnerUser_IdUserAndBloodTypeOrderByIdHealthProfileAsc(Long idUser, String bloodType);

    // Consulta nativa: perfiles activos de la cuenta autenticada. El ID se obtiene del JWT.
    @Query(value = "SELECT hp.* FROM health_profiles hp " +
            "INNER JOIN users u ON u.id_user = hp.id_owner_user " +
            "WHERE u.id_user = :userId AND hp.active = true " +
            "ORDER BY hp.id_health_profile", nativeQuery = true)
    List<HealthProfile> getActiveProfilesByOwner(@Param("userId") Long userId);
}

package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.HealthProfile;
import pe.edu.upc.latia_lati.entities.Users;

import java.util.List;

@Repository
public interface IHealthProfileRepository extends JpaRepository<HealthProfile, Long> {
    List<HealthProfile> findByOwnerUserIn(List<Users> users);

    // Consulta simple: grupo sanguíneo dentro de los perfiles del dueño autenticado.
    List<HealthProfile> findByOwnerUser_IdUserAndBloodTypeOrderByIdHealthProfileAsc(
            Long idUser, String bloodType);

    // Consulta nativa: perfiles activos del dueño autenticado.
    @Query(value = "SELECT hp.* FROM health_profiles hp " +
            "INNER JOIN users u ON u.id_user = hp.id_owner_user " +
            "WHERE u.id_user = :userId AND hp.active = true " +
            "ORDER BY hp.id_health_profile", nativeQuery = true)
    List<HealthProfile> getActiveProfilesByOwner(@Param("userId") Long userId);

}

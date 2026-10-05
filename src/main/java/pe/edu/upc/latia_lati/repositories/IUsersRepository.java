package pe.edu.upc.latia_lati.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.Users;

import java.util.Optional;

@Repository
public interface IUsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    @Query(value = "SELECT setval(pg_get_serial_sequence('users', 'id_user'), COALESCE((SELECT MAX(id_user) FROM users), 0), true)", nativeQuery = true)
    void syncIdSequence();

    // Consulta simple: filtra por active y ordena por ID.
    List<Users> findByActiveOrderByIdUserAsc(Boolean active);

    // Consulta nativa: cuenta perfiles por usuario, incluyendo usuarios con cero perfiles.
    @Query(value = "SELECT u.id_user, u.first_name, u.last_name, u.username, COUNT(hp.id_health_profile) " +
            "FROM users u " +
            "LEFT JOIN health_profiles hp ON hp.id_owner_user = u.id_user " +
            "GROUP BY u.id_user, u.first_name, u.last_name, u.username " +
            "ORDER BY u.id_user", nativeQuery = true)
    List<Object[]> getTotalHealthProfilesByUser();

}

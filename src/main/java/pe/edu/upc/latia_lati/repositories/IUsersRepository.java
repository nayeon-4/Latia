package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.Users;
import java.util.List;
import java.util.Optional;

@Repository
public interface IUsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username); // Login: sí se utiliza.
    boolean existsByUsername(String username);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsernameAndIdUserNot(String username, Long idUser);
    boolean existsByEmailIgnoreCaseAndIdUserNot(String email, Long idUser);

    // Consulta simple: Spring Data genera el SQL a partir del nombre del método.
    List<Users> findByActiveOrderByIdUserAsc(Boolean active);

    // Consulta nativa: SQL explícito, con JOIN y agrupación. Incluye usuarios con cero perfiles.
    @Query(value = "SELECT u.id_user, u.first_name, u.last_name, u.username, COUNT(hp.id_health_profile) " +
            "FROM users u " +
            "LEFT JOIN health_profiles hp ON hp.id_owner_user = u.id_user " +
            "GROUP BY u.id_user, u.first_name, u.last_name, u.username " +
            "ORDER BY u.id_user", nativeQuery = true)
    List<Object[]> getTotalHealthProfilesByUser();

    // Lo usa DemoDataInitializer; no es una consulta funcional para el usuario.
    @Query(value = "SELECT setval(pg_get_serial_sequence('users', 'id_user'), GREATEST(COALESCE((SELECT MAX(id_user) FROM users), 0), 1), EXISTS(SELECT 1 FROM users))", nativeQuery = true)
    Long syncIdSequence();
}

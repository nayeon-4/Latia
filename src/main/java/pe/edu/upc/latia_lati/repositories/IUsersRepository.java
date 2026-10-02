package pe.edu.upc.latia_lati.repositories;

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
}

package pe.edu.upc.latia_lati.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.Users;
import java.util.Optional;
@Repository
public interface IUsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmailIgnoreCase(String email);
    boolean existsByUsernameAndIdUserNot(String username, Long idUser);
    boolean existsByEmailIgnoreCaseAndIdUserNot(String email, Long idUser);
}

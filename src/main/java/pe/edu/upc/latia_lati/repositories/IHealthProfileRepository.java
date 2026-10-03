package pe.edu.upc.latia_lati.repositories;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.HealthProfile;
@Repository
public interface IHealthProfileRepository extends JpaRepository<HealthProfile, Long> {
    List<HealthProfile> findByOwnerUser_IdUserOrderByIdHealthProfileAsc(Long idUser);
    boolean existsByHolderUser_IdUser(Long idUser);
    boolean existsByHolderUser_IdUserAndIdHealthProfileNot(Long idUser, Long idProfile);
}

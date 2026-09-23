package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.EmergencyContact;

@Repository
public interface IEmergencyContactRepository extends JpaRepository<EmergencyContact, Long> {
}

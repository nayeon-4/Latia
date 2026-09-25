package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.Medications;

@Repository
public interface IMedicationsRepository extends JpaRepository<Medications, Long> {

}

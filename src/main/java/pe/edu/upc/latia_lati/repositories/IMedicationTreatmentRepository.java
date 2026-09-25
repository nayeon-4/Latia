package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.MedicationTreatment;

@Repository
public interface IMedicationTreatmentRepository extends JpaRepository<MedicationTreatment, Long> {
}

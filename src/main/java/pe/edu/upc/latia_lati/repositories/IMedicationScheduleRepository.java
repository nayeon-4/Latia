package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.MedicationSchedule;

@Repository
public interface IMedicationScheduleRepository extends JpaRepository<MedicationSchedule, Long> {
}

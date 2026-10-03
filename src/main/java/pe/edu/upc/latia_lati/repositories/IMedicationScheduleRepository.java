package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.MedicationSchedule;
import pe.edu.upc.latia_lati.entities.MedicationTreatment;

import java.util.List;

@Repository
public interface IMedicationScheduleRepository extends JpaRepository<MedicationSchedule, Long> {
    List<MedicationSchedule> findByTreatmentIn(List<MedicationTreatment> treatments);
    List<MedicationSchedule> findByTreatment_Id(Long treatmentId);
}

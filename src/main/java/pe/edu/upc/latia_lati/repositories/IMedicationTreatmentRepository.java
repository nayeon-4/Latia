package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.MedicationTreatment;
import pe.edu.upc.latia_lati.entities.ClinicalRecord;
import pe.edu.upc.latia_lati.entities.Medications;

import java.util.List;

@Repository
public interface IMedicationTreatmentRepository extends JpaRepository<MedicationTreatment, Long> {
    List<MedicationTreatment> findByClinicalRecordIn(List<ClinicalRecord> clinicalRecords);
    boolean existsByMedication(Medications medication);
}

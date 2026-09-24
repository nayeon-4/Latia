package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.ClinicalRecord;

import java.util.List;

@Repository
public interface IClinicalRecordRepository extends JpaRepository<ClinicalRecord, Long> {
    public List<ClinicalRecord> findByRecordType(String recordType);
    @Query(value = "SELECT mc.name, COUNT(cr.id) " +
            "FROM medical_conditions mc " +
            "LEFT JOIN clinical_records cr " +
            "ON mc.id = cr.condition_id " +
            "LEFT JOIN health_profiles hp " +
            "ON hp.id = cr.health_profile_id " +
            "GROUP BY mc.id, mc.name",
            nativeQuery = true)
    List<Object[]> getTotalClinicalRecordsByCondition();
}

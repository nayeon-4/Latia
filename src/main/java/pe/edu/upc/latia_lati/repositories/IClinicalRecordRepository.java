package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.ClinicalRecord;
import pe.edu.upc.latia_lati.entities.HealthProfile;
import pe.edu.upc.latia_lati.entities.MedicalCondition;

import java.util.List;

@Repository
public interface IClinicalRecordRepository extends JpaRepository<ClinicalRecord, Long> {
    List<ClinicalRecord> findByHealthProfileIn(List<HealthProfile> healthProfiles);
    boolean existsByMedicalCondition(MedicalCondition medicalCondition);
    public List<ClinicalRecord> findByRecordType(String recordType);
    @Query(value = "SELECT mc.name_medical_condition, COUNT(cr.id_clinical_record) " +
            "FROM medical_conditions mc " +
            "LEFT JOIN clinical_records cr " +
            "ON mc.id_medical_condition = cr.id_condition " +
            "GROUP BY mc.id_medical_condition, mc.name_medical_condition",
            nativeQuery = true)
    List<Object[]> getTotalClinicalRecordsByCondition();
}

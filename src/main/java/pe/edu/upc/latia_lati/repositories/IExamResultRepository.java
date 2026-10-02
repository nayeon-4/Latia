package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.ExamResult;
import pe.edu.upc.latia_lati.entities.ClinicalRecord;

import java.util.List;

@Repository
public interface IExamResultRepository extends JpaRepository<ExamResult, Long> {
    List<ExamResult> findByClinicalRecordIn(List<ClinicalRecord> clinicalRecords);
    List<ExamResult> findByClinicalRecord_IdClinicalRecord(Long clinicalRecordId);
}

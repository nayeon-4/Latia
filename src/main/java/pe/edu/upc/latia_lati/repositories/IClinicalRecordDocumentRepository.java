package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.ClinicalRecordDocument;
import pe.edu.upc.latia_lati.entities.ClinicalRecord;
import pe.edu.upc.latia_lati.entities.MedicalDocuments;

import java.util.List;

@Repository
public interface IClinicalRecordDocumentRepository extends JpaRepository<ClinicalRecordDocument, Long> {
    List<ClinicalRecordDocument> findByClinicalRecordIn(List<ClinicalRecord> clinicalRecords);
    List<ClinicalRecordDocument> findByMedicalDocumentIn(List<MedicalDocuments> medicalDocuments);
    List<ClinicalRecordDocument> findByClinicalRecord_IdClinicalRecord(Long clinicalRecordId);
}

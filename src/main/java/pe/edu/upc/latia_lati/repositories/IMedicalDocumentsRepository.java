package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.MedicalDocuments;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IMedicalDocumentsRepository extends JpaRepository<MedicalDocuments, Long> {
    public List<MedicalDocuments> findByDocumentDate(LocalDate documentDate);
    @Query(value = """
    SELECT md.*
    FROM medical_documents md
    JOIN health_profiles hp
      ON hp.id_health_profile = md.id_health_profile
    WHERE hp.id_owner_user = :userId
    """, nativeQuery = true)
    List<MedicalDocuments> buscarDocumentosPorUsuario(@Param("userId") Long userId);
}

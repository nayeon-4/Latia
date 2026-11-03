package pe.edu.upc.latia_lati.serviceinterfaces;

import pe.edu.upc.latia_lati.entities.ClinicalRecordDocument;

import java.util.List;
import java.util.Optional;

public interface IClinicalRecordDocumentService {
    public void insert(ClinicalRecordDocument clinicalRecordDocument);
    public List<ClinicalRecordDocument> list();
    public void update(ClinicalRecordDocument clinicalRecordDocument);
    public void delete(Long idClinicalRecord);
    public Optional<ClinicalRecordDocument> listId(Long id);
}

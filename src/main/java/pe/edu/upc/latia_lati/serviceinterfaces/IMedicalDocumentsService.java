package pe.edu.upc.latia_lati.serviceinterfaces;

import pe.edu.upc.latia_lati.entities.MedicalDocuments;

import java.util.List;
import java.util.Optional;

public interface IMedicalDocumentsService {
    public void insert (MedicalDocuments medicalDocuments);
    public List<MedicalDocuments> list();
    public void update(MedicalDocuments medicalDocuments);
    public void delete(Long idMedicalDocuments);
    public Optional<MedicalDocuments> listId(Long id);
}

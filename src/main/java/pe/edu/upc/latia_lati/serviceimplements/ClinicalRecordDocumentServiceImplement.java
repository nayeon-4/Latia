package pe.edu.upc.latia_lati.serviceimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.ClinicalRecordDocument;
import pe.edu.upc.latia_lati.repositories.IClinicalRecordDocumentRepository;
import pe.edu.upc.latia_lati.serviceinterfaces.IClinicalRecordDocumentService;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicalRecordDocumentServiceImplement implements IClinicalRecordDocumentService {

    private final IClinicalRecordDocumentRepository crdR;

    public ClinicalRecordDocumentServiceImplement(IClinicalRecordDocumentRepository crdR) {
        this.crdR = crdR;
    }

    @Override
    public void insert(ClinicalRecordDocument clinicalRecordDocument) {
        crdR.save(clinicalRecordDocument);
    }

    @Override
    public List<ClinicalRecordDocument> list() {
        return crdR.findAll();
    }

    @Override
    public void update(ClinicalRecordDocument clinicalRecordDocument) {
        crdR.save(clinicalRecordDocument);
    }

    @Override
    public void delete(Long id) {
        crdR.deleteById(id);
    }

    @Override
    public Optional<ClinicalRecordDocument> listId(Long id) {
        return crdR.findById(id);
    }

}
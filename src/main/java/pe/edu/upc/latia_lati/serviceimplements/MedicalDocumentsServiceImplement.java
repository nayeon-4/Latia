package pe.edu.upc.latia_lati.serviceimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.MedicalDocuments;
import pe.edu.upc.latia_lati.repositories.IMedicalDocumentsRepository;
import pe.edu.upc.latia_lati.serviceinterfaces.IMedicalDocumentsService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MedicalDocumentsServiceImplement implements IMedicalDocumentsService {
    private final IMedicalDocumentsRepository mDR;
    public MedicalDocumentsServiceImplement(IMedicalDocumentsRepository mDR) {
        this.mDR = mDR;
    }
    @Override
    public void insert(MedicalDocuments medicalDocuments) {
        mDR.save(medicalDocuments);
    }

    @Override
    public List<MedicalDocuments> list() {
        return mDR.findAll();
    }

    @Override
    public void update(MedicalDocuments medicalDocuments) {
        mDR.save(medicalDocuments);
    }

    @Override
    public void delete(Long idMedicalDocuments) {
        mDR.deleteById(idMedicalDocuments);
    }



    @Override
    public Optional<MedicalDocuments> listId(Long id) {
        return mDR.findById(id);
    }

    @Override
    public List<MedicalDocuments> buscarDocumentosPorFecha(LocalDate documentDate) {
        return mDR.findByDocumentDate(documentDate);
    }

    @Override
    public List<MedicalDocuments> buscarDocumentosPorUsuario(Long userId) {
        return mDR.buscarDocumentosPorUsuario(userId);
    }
}

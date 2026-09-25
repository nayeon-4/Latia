package pe.edu.upc.latia_lati.serviceimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.ClinicalRecord;
import pe.edu.upc.latia_lati.repositories.IClinicalRecordRepository;
import pe.edu.upc.latia_lati.serviceinterfaces.IClinicalRecordService;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicalRecordImplement implements IClinicalRecordService {
    private final IClinicalRecordRepository crS;

    public ClinicalRecordImplement(IClinicalRecordRepository crS) {
        this.crS = crS;
    }

    @Override
    public void insert(ClinicalRecord clinicalRecord) {
        crS.save(clinicalRecord);
    }

    @Override
    public List<ClinicalRecord> list() {
        return crS.findAll();
    }

    @Override
    public void update(ClinicalRecord clinicalRecord) {
        crS.save(clinicalRecord);
    }

    @Override
    public void delete(Long idClinicalRecord) {
        crS.deleteById(idClinicalRecord);
    }

    @Override
    public Optional<ClinicalRecord> listId(Long id) {
        return crS.findById(id);
    }
}

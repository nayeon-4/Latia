package pe.edu.upc.latia_lati.serviceimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.ClinicalRecord;
import pe.edu.upc.latia_lati.repositories.IClinicalRecordRepository;
import pe.edu.upc.latia_lati.serviceinterfaces.IClinicalRecordService;

import java.util.List;
import java.util.Optional;

@Service
public class ClinicalRecordImplement implements IClinicalRecordService {
    private final IClinicalRecordRepository crR;

    public ClinicalRecordImplement(IClinicalRecordRepository crR) {
        this.crR = crR;
    }

    @Override
    public void insert(ClinicalRecord clinicalRecord) {
        crR.save(clinicalRecord);
    }

    @Override
    public List<ClinicalRecord> list() {
        return crR.findAll();
    }

    @Override
    public void update(ClinicalRecord clinicalRecord) {
        crR.save(clinicalRecord);
    }

    @Override
    public void delete(Long idClinicalRecord) {
        crR.deleteById(idClinicalRecord);
    }

    @Override
    public Optional<ClinicalRecord> listId(Long id) {
        return crR.findById(id);
    }

    @Override
    public List<ClinicalRecord> obtenerPorTipo(String recordType) {
        return crR.findByRecordType(recordType);
    }

    @Override
    public List<Object[]> cantidadPorCondicionMedica() {
        return crR.getTotalClinicalRecordsByCondition();
    }
}

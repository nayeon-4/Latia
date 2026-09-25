package pe.edu.upc.latia_lati.serviceinterfaces;

import pe.edu.upc.latia_lati.entities.ClinicalRecord;

import java.util.List;
import java.util.Optional;

public interface IClinicalRecordService {
    public void insert(ClinicalRecord clinicalRecord);
    public List<ClinicalRecord> list();
    public void update(ClinicalRecord clinicalRecord);
    public void delete(Long idClinicalRecord);
    public Optional<ClinicalRecord> listId(Long id);
    public List<ClinicalRecord> obtenerPorTipo(String tipo);
    public List<Object[]> cantidadPorCondicionMedica();
}

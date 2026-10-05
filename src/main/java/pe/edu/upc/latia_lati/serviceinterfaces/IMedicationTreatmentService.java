package pe.edu.upc.latia_lati.serviceinterfaces;

import pe.edu.upc.latia_lati.entities.MedicationTreatment;

import java.util.List;
import java.util.Optional;

public interface IMedicationTreatmentService {
    void insert(MedicationTreatment treatment);
    List<MedicationTreatment> list();
    void update(MedicationTreatment treatment);
    void delete(Long id);
    Optional<MedicationTreatment> listId(Long id);
}

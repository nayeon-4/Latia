package pe.edu.upc.latia_lati.serviceinterfaces;

import pe.edu.upc.latia_lati.entities.MedicationSchedule;

import java.util.List;
import java.util.Optional;

public interface IMedicationScheduleService {
    void insert(MedicationSchedule schedule);
    List<MedicationSchedule> list();
    void update(MedicationSchedule schedule);
    void delete(Long id);
    Optional<MedicationSchedule> listId(Long id);
    List<MedicationSchedule> listarPorTratamiento(Long treatmentId);
}

package pe.edu.upc.latia_lati.serviceimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.MedicationSchedule;
import pe.edu.upc.latia_lati.repositories.IMedicationScheduleRepository;
import pe.edu.upc.latia_lati.serviceinterfaces.IMedicationScheduleService;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationScheduleServiceImplement implements IMedicationScheduleService {
    private final IMedicationScheduleRepository scheduleRepository;

    public MedicationScheduleServiceImplement(IMedicationScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public void insert(MedicationSchedule schedule) {
        scheduleRepository.save(schedule);
    }

    @Override
    public List<MedicationSchedule> list() {
        return scheduleRepository.findAll();
    }

    @Override
    public void update(MedicationSchedule schedule) {
        scheduleRepository.save(schedule);
    }

    @Override
    public void delete(Long id) {
        scheduleRepository.deleteById(id);
    }

    @Override
    public Optional<MedicationSchedule> listId(Long id) {
        return scheduleRepository.findById(id);
    }

    @Override
    public List<MedicationSchedule> listarPorTratamiento(Long treatmentId) {
        return scheduleRepository.findByTreatment_Id(treatmentId);
    }
}

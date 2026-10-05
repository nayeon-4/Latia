package pe.edu.upc.latia_lati.serviceimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.MedicationTreatment;
import pe.edu.upc.latia_lati.repositories.IMedicationTreatmentRepository;
import pe.edu.upc.latia_lati.serviceinterfaces.IMedicationTreatmentService;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationTreatmentServiceImplement implements IMedicationTreatmentService {
    private final IMedicationTreatmentRepository treatmentRepository;

    public MedicationTreatmentServiceImplement(IMedicationTreatmentRepository treatmentRepository) {
        this.treatmentRepository = treatmentRepository;
    }

    @Override
    public void insert(MedicationTreatment treatment) {
        treatmentRepository.save(treatment);
    }

    @Override
    public List<MedicationTreatment> list() {
        return treatmentRepository.findAll();
    }

    @Override
    public void update(MedicationTreatment treatment) {
        treatmentRepository.save(treatment);
    }

    @Override
    public void delete(Long id) {
        treatmentRepository.deleteById(id);
    }

    @Override
    public Optional<MedicationTreatment> listId(Long id) {
        return treatmentRepository.findById(id);
    }
}

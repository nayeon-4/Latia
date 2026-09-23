package pe.edu.upc.latia_lati.serviceimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.MedicalCondition;
import pe.edu.upc.latia_lati.repositories.IMedicalConditionRepository;
import pe.edu.upc.latia_lati.serviceinterfaces.IMedicalConditionService;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalConditionImplement implements IMedicalConditionService {
    private final IMedicalConditionRepository mcR;

    public MedicalConditionImplement(IMedicalConditionRepository mcR) {
        this.mcR = mcR;
    }

    @Override
    public void insert(MedicalCondition medicalCondition) {
        mcR.save(medicalCondition);
    }

    @Override
    public List<MedicalCondition> list() {
        return mcR.findAll();
    }

    @Override
    public void update(MedicalCondition medicalCondition) {
        mcR.save(medicalCondition);
    }

    @Override
    public void delete(Long idMedicalCondition) {
        mcR.deleteById(idMedicalCondition);
    }

    @Override    public Optional<MedicalCondition> listId(Long id) {
        return mcR.findById(id);
    }
}

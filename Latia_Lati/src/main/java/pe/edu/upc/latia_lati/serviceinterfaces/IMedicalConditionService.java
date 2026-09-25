package pe.edu.upc.latia_lati.serviceinterfaces;

import pe.edu.upc.latia_lati.entities.MedicalCondition;

import java.util.List;
import java.util.Optional;

public interface IMedicalConditionService {
    public void insert(MedicalCondition medicalCondition);
    public List<MedicalCondition> list();
    public void update(MedicalCondition medicalCondition);
    public void delete(Long idMedicalCondition);
    public Optional<MedicalCondition> listId(Long id);
}

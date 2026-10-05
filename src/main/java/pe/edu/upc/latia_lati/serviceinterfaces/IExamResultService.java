package pe.edu.upc.latia_lati.serviceinterfaces;

import pe.edu.upc.latia_lati.entities.ExamResult;

import java.util.List;
import java.util.Optional;

public interface IExamResultService {
    void insert(ExamResult examResult);
    List<ExamResult> list();
    void update(ExamResult examResult);
    void delete(Long id);
    Optional<ExamResult> listId(Long id);
    List<ExamResult> listarPorHistoriaClinica(Long clinicalRecordId);
}

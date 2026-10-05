package pe.edu.upc.latia_lati.serviceimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.ExamResult;
import pe.edu.upc.latia_lati.repositories.IExamResultRepository;
import pe.edu.upc.latia_lati.serviceinterfaces.IExamResultService;

import java.util.List;
import java.util.Optional;

@Service
public class ExamResultServiceImplement implements IExamResultService {
    private final IExamResultRepository examResultRepository;

    public ExamResultServiceImplement(IExamResultRepository examResultRepository) {
        this.examResultRepository = examResultRepository;
    }

    @Override
    public void insert(ExamResult examResult) {
        examResultRepository.save(examResult);
    }

    @Override
    public List<ExamResult> list() {
        return examResultRepository.findAll();
    }

    @Override
    public void update(ExamResult examResult) {
        examResultRepository.save(examResult);
    }

    @Override
    public void delete(Long id) {
        examResultRepository.deleteById(id);
    }

    @Override
    public Optional<ExamResult> listId(Long id) {
        return examResultRepository.findById(id);
    }

    @Override
    public List<ExamResult> listarPorHistoriaClinica(Long clinicalRecordId) {
        return examResultRepository.findByClinicalRecord_IdClinicalRecord(clinicalRecordId);
    }
}

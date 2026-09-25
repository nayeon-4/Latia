package pe.edu.upc.latia_lati.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.edu.upc.latia_lati.entities.ExamResult;

@Repository
public interface IExamResultRepository extends JpaRepository<ExamResult, Long> {
}

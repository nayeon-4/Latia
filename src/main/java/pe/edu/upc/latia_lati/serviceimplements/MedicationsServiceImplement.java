package pe.edu.upc.latia_lati.serviceimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.Medications;
import pe.edu.upc.latia_lati.repositories.IMedicationsRepository;
import pe.edu.upc.latia_lati.serviceinterfaces.IMedicationsService;

import java.util.List;
import java.util.Optional;

@Service
public class MedicationsServiceImplement implements IMedicationsService {
    private final IMedicationsRepository mR;

    public MedicationsServiceImplement(IMedicationsRepository mR) {
        this.mR = mR;
    }

    @Override
    public void insert(Medications medications) {
        mR.save(medications);
    }

    @Override
    public List<Medications> list() {
        return mR.findAll();
    }

    @Override
    public void update(Medications medications) {
        mR.save(medications);
    }


    @Override
    public void delete(Long id) {
        mR.deleteById(id);
    }

    @Override
    public Optional<Medications> listId(Long id) {
        return mR.findById(id);
    }
}

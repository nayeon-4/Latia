package pe.edu.upc.latia_lati.serviceimplements;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.*;
import pe.edu.upc.latia_lati.repositories.*;
import pe.edu.upc.latia_lati.exceptions.*;
import pe.edu.upc.latia_lati.serviceinterfaces.IHealthProfileService;

@Service
public class HealthProfileServiceImplement implements IHealthProfileService {
    private final IHealthProfileRepository hpR;

    public HealthProfileServiceImplement(IHealthProfileRepository hpR) {
        this.hpR = hpR;
    }

    @Override
    public void insert(HealthProfile healthProfile) {
        hpR.save(healthProfile);
    }

    @Override
    public List<HealthProfile> list() {
        return hpR.findAll();
    }

    @Override
    public void update(HealthProfile healthProfile) {
        hpR.save(healthProfile);
    }

    @Override
    public void delete(Long idHealthProfile) {
        hpR.deleteById(idHealthProfile);
    }

    @Override
    public Optional<HealthProfile> listId(Long id) {
        return hpR.findById(id);
    }
}

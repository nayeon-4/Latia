package pe.edu.upc.latia_lati.serviceimplements;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.*;
import pe.edu.upc.latia_lati.repositories.*;
import pe.edu.upc.latia_lati.exceptions.*;
import pe.edu.upc.latia_lati.serviceinterfaces.IEmergencyContactService;

@Service
public class EmergencyContactServiceImplement implements IEmergencyContactService {
    private final IEmergencyContactRepository ecR;

    public EmergencyContactServiceImplement(IEmergencyContactRepository ecR) {
        this.ecR = ecR;
    }

    @Override
    public void insert(EmergencyContact emergencyContact) {
        ecR.save(emergencyContact);
    }

    @Override
    public List<EmergencyContact> list() {
        return ecR.findAll();
    }

    @Override
    public void update(EmergencyContact emergencyContact) {
        ecR.save(emergencyContact);
    }

    @Override
    public void delete(Long idEmergencyContact) {
        ecR.deleteById(idEmergencyContact);
    }

    @Override
    public Optional<EmergencyContact> listId(Long id) {
        return ecR.findById(id);
    }
}

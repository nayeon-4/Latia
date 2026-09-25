package pe.edu.upc.latia_lati.serviceinterfaces;

import java.util.List;
import java.util.Optional;

import pe.edu.upc.latia_lati.entities.EmergencyContact;

public interface IEmergencyContactService {
    public void insert(EmergencyContact emergencyContact);
    public List<EmergencyContact> list();
    public void update(EmergencyContact emergencyContact);
    public void delete(Long idEmergencyContact);
    public Optional<EmergencyContact> listId(Long id);
}

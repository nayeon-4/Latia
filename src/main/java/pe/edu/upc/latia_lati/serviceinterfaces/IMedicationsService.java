package pe.edu.upc.latia_lati.serviceinterfaces;

import pe.edu.upc.latia_lati.entities.Medications;

import java.util.List;
import java.util.Optional;

public interface IMedicationsService {
    public void insert(Medications medications);
    public List<Medications> list();
    public void update(Medications medications);
    public void delete(Long id);
    public Optional<Medications> listId(Long id);
}

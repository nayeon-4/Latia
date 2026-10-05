package pe.edu.upc.latia_lati.serviceinterfaces;

import java.util.List;
import java.util.Optional;

import pe.edu.upc.latia_lati.entities.HealthProfile;

public interface IHealthProfileService {
    public void insert(HealthProfile healthProfile);
    public List<HealthProfile> list();
    public void update(HealthProfile healthProfile);
    public void delete(Long idHealthProfile);
    public Optional<HealthProfile> listId(Long id);

    // Consultas adicionales de los perfiles propios.
    public List<HealthProfile> obtenerPorTipoSangre(String bloodType);
    public List<HealthProfile> listarActivosPropios();

}

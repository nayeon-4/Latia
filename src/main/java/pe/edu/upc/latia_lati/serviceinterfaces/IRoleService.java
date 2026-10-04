package pe.edu.upc.latia_lati.serviceinterfaces;

import pe.edu.upc.latia_lati.entities.Role;

import java.util.List;
import java.util.Optional;

public interface IRoleService {
    public void insert(Role role);
    public List<Role> list();
    public void update(Role role);
    public void delete(Long idRole);
    public Optional<Role> listId(Long id);
}

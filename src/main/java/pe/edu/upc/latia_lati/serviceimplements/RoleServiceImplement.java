package pe.edu.upc.latia_lati.serviceimplements;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.Role;
import pe.edu.upc.latia_lati.repositories.IRoleRepository;
import pe.edu.upc.latia_lati.serviceinterfaces.IRoleService;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImplement implements IRoleService {
    private final IRoleRepository rR;

    public RoleServiceImplement(IRoleRepository rR) {
        this.rR = rR;
    }

    @Override
    public void insert(Role role) {
        rR.save(role);
    }

    @Override
    public List<Role> list() {
        return rR.findAll();
    }

    @Override
    public void update(Role role) {
        rR.save(role);
    }

    @Override
    public void delete(Long idRole) {
        rR.deleteById(idRole);
    }

    @Override
    public Optional<Role> listId(Long id) {
        return rR.findById(id);
    }
}

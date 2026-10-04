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
    private final pe.edu.upc.latia_lati.securities.CurrentUser current;

    public RoleServiceImplement(IRoleRepository rR, pe.edu.upc.latia_lati.securities.CurrentUser current) {
        this.rR = rR;
        this.current = current;
    }

    @Override
    public void insert(Role role) {
        current.requireAdmin();
        rR.save(role);
    }

    @Override
    public List<Role> list() {
        current.requireAdmin();
        return rR.findAll();
    }

    @Override
    public void update(Role role) {
        current.requireAdmin();
        rR.save(role);
    }

    @Override
    public void delete(Long idRole) {
        current.requireAdmin();
        rR.deleteById(idRole);
    }

    @Override
    public Optional<Role> listId(Long id) {
        current.requireAdmin();
        return rR.findById(id);
    }
}

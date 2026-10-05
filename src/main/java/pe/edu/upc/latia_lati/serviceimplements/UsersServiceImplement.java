package pe.edu.upc.latia_lati.serviceimplements;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.latia_lati.exceptions.BusinessRuleException;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.*;
import pe.edu.upc.latia_lati.repositories.*;
import pe.edu.upc.latia_lati.serviceinterfaces.IUsersService;

@Service
public class UsersServiceImplement implements IUsersService {
    private final IUsersRepository uR;


    public UsersServiceImplement(IUsersRepository uR) {
        this.uR = uR;
    }

    @Override
    public void insert(Users user) {
        uR.save(user);
    }

    @Override
    public List<Users> list() {
        return uR.findAll();
    }

    @Override
    public void update(Users user) {
        uR.save(user);
    }

    @Override
    public void delete(Long idUser) {
        uR.deleteById(idUser);
    }

    @Override
    public Optional<Users> listId(Long id) {
        return uR.findById(id);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ADMIN')")
    @Transactional(readOnly = true)
    public List<Users> obtenerPorEstado(Boolean active) {
        if (active == null) {
            throw new BusinessRuleException("El parámetro active es obligatorio: usa true o false");
        }
        List<Users> resultado = uR.findByActiveOrderByIdUserAsc(active);
        if (resultado.isEmpty()) {
            throw new ResourceNotFoundException("No hay usuarios con active=" + active);
        }
        return resultado;
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ADMIN')")
    @Transactional(readOnly = true)
    public List<Object[]> cantidadPerfilesPorUsuario() {
        List<Object[]> resultado = uR.getTotalHealthProfilesByUser();
        if (resultado.isEmpty()) {
            throw new ResourceNotFoundException("No hay usuarios para el reporte de perfiles");
        }
        return resultado;
    }

}

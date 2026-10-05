package pe.edu.upc.latia_lati.serviceimplements;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

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
    private final IUsersRepository uR;

    public HealthProfileServiceImplement(IHealthProfileRepository hpR, IUsersRepository uR) {
        this.hpR = hpR;
        this.uR = uR;
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

    @Override
    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public List<HealthProfile> obtenerPorTipoSangre(String bloodType) {
        if (bloodType == null || !bloodType.matches("^(A|B|AB|O)[+-]$")) {
            throw new BusinessRuleException("Tipo de sangre inválido: usa A+, A-, B+, B-, AB+, AB-, O+ u O-");
        }
        List<HealthProfile> resultado = hpR.findByOwnerUser_IdUserAndBloodTypeOrderByIdHealthProfileAsc(
                obtenerIdPropietarioAutenticado(), bloodType);
        if (resultado.isEmpty()) {
            throw new ResourceNotFoundException("No tienes perfiles con el grupo sanguíneo " + bloodType);
        }
        return resultado;
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    @Transactional(readOnly = true)
    public List<HealthProfile> listarActivosPropios() {
        List<HealthProfile> resultado = hpR.getActiveProfilesByOwner(obtenerIdPropietarioAutenticado());
        if (resultado.isEmpty()) {
            throw new ResourceNotFoundException("No tienes perfiles de salud activos");
        }
        return resultado;
    }

    // main identifica la sesión por username: se obtiene su ID sin modificar el JWT.
    private Long obtenerIdPropietarioAutenticado() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("Inicia sesión para consultar tus perfiles");
        }
        Users propietario = uR.findByUsername(auth.getName())
                .orElseThrow(() -> new AccessDeniedException("La cuenta de la sesión ya no existe"));
        if (!Boolean.TRUE.equals(propietario.getActive())) {
            throw new AccessDeniedException("La cuenta está inactiva");
        }
        return propietario.getIdUser();
    }

}

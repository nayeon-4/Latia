package pe.edu.upc.latia_lati.serviceimplements;

import java.util.List;
import java.util.Optional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.upc.latia_lati.dtos.*;
import pe.edu.upc.latia_lati.entities.*;
import pe.edu.upc.latia_lati.exceptions.*;
import pe.edu.upc.latia_lati.repositories.IHealthProfileRepository;
import pe.edu.upc.latia_lati.securities.CurrentUser;
import pe.edu.upc.latia_lati.serviceinterfaces.IHealthProfileService;

@Service
@Validated
@Transactional(readOnly = true)
public class HealthProfileServiceImplement implements IHealthProfileService {
    private final IHealthProfileRepository profiles;
    private final CurrentUser current;
    public HealthProfileServiceImplement(IHealthProfileRepository profiles, CurrentUser current) {
        this.profiles = profiles;
        this.current = current;
    }

    @Override @Transactional
    public HealthProfileDTO create(HealthProfileRequestDTO r) {
        Users actor = current.require();
        validateLinks(r, actor, null);
        HealthProfile hp = new HealthProfile();
        hp.setOwnerUser(actor);
        apply(hp, r, actor);
        return toDTO(profiles.saveAndFlush(hp));
    }

    @Override
    public List<HealthProfileDTO> list() {
        Users actor = current.require();
        List<HealthProfile> result = profiles.findByOwnerUser_IdUserOrderByIdHealthProfileAsc(actor.getIdUser());
        if (result.isEmpty()) throw new ResourceNotFoundException("No tienes perfiles de salud registrados");
        return result.stream().map(this::toDTO).toList();
    }

    @Override
    public List<HealthProfileDTO> findByBloodType(String bloodType) {
        Long ownerId = current.require().getIdUser();
        List<HealthProfile> result = profiles.findByOwnerUser_IdUserAndBloodTypeOrderByIdHealthProfileAsc(ownerId, bloodType);
        if (result.isEmpty()) throw new ResourceNotFoundException("No tienes perfiles con el grupo sanguíneo " + bloodType);
        return result.stream().map(this::toDTO).toList();
    }

    @Override
    public List<HealthProfileDTO> activeProfiles() {
        List<HealthProfile> result = profiles.getActiveProfilesByOwner(current.require().getIdUser());
        if (result.isEmpty()) throw new ResourceNotFoundException("No tienes perfiles de salud activos");
        return result.stream().map(this::toDTO).toList();
    }

    @Override
    public HealthProfileDTO find(Long id) { return toDTO(getOwned(id)); }

    @Override @Transactional
    public HealthProfileDTO update(Long id, HealthProfileRequestDTO r) {
        HealthProfile hp = getOwned(id);
        Users actor = current.require();
        validateLinks(r, actor, id);
        apply(hp, r, actor);
        return toDTO(profiles.saveAndFlush(hp));
    }

    @Override @Transactional
    public void delete(Long id) {
        profiles.delete(getOwned(id));
        profiles.flush(); // No se borran historias médicas en cascada.
    }

    @Override
    public Optional<HealthProfile> listId(Long id) {
        Users actor = current.require();
        return profiles.findById(id).filter(hp -> hp.getOwnerUser().getIdUser().equals(actor.getIdUser()));
    }

    private HealthProfile getOwned(Long id) {
        return listId(id).orElseThrow(() -> new ResourceNotFoundException("No existe un perfil accesible con el id: " + id));
    }
    private void validateLinks(HealthProfileRequestDTO r, Users actor, Long profileId) {
        if (r.getIdOwnerUser() != null && !actor.getIdUser().equals(r.getIdOwnerUser())) {
            throw new AccessDeniedException("El propietario debe ser el usuario autenticado");
        }
        // Vincular cuentas ajenas requiere un futuro flujo de consentimiento, fuera de HU01–HU10.
        if (r.getIdHolderUser() != null && !actor.getIdUser().equals(r.getIdHolderUser())) {
            throw new AccessDeniedException("Solo puedes vincular tu propia cuenta como titular");
        }
        if (r.getIdHolderUser() != null) {
            boolean duplicate = profileId == null ? profiles.existsByHolderUser_IdUser(r.getIdHolderUser())
                    : profiles.existsByHolderUser_IdUserAndIdHealthProfileNot(r.getIdHolderUser(), profileId);
            if (duplicate) throw new ConflictException("La cuenta titular ya tiene un perfil de salud");
        }
    }
    private void apply(HealthProfile hp, HealthProfileRequestDTO r, Users actor) {
        hp.setFirstName(r.getFirstName().trim());
        hp.setLastName(r.getLastName().trim());
        hp.setHolderUser(r.getIdHolderUser() == null ? null : actor);
        hp.setBirthDate(r.getBirthDate());
        hp.setSex(r.getSex().trim());
        hp.setBloodType(r.getBloodType());
        hp.setPhone(r.getPhone().trim());
        hp.setActive(r.getActive());
    }
    // Nombres del paciente e IDs explícitos: no se depende de ModelMapper.
    private HealthProfileDTO toDTO(HealthProfile hp) {
        return new HealthProfileDTO(hp.getIdHealthProfile(), hp.getFirstName(), hp.getLastName(),
                hp.getOwnerUser().getIdUser(), hp.getHolderUser() == null ? null : hp.getHolderUser().getIdUser(),
                hp.getBirthDate(), hp.getSex(), hp.getBloodType(), hp.getPhone(), hp.getActive(),
                hp.getCreatedAt(), hp.getUpdatedAt());
    }
}

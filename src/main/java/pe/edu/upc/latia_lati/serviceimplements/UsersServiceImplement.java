package pe.edu.upc.latia_lati.serviceimplements;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import pe.edu.upc.latia_lati.dtos.*;
import pe.edu.upc.latia_lati.entities.*;
import pe.edu.upc.latia_lati.exceptions.*;
import pe.edu.upc.latia_lati.repositories.IUsersRepository;
import pe.edu.upc.latia_lati.securities.CurrentUser;
import pe.edu.upc.latia_lati.serviceinterfaces.IUsersService;

@Service
@Validated
@Transactional(readOnly = true)
public class UsersServiceImplement implements IUsersService {
    private final IUsersRepository users;
    private final PasswordEncoder passwords;
    private final CurrentUser current;
    public UsersServiceImplement(IUsersRepository users, PasswordEncoder passwords, CurrentUser current) {
        this.users = users;
        this.passwords = passwords;
        this.current = current;
    }

    @Override @Transactional
    public UsersResponseDTO register(UsersRequestDTO r) {
        String email = r.getEmail().trim().toLowerCase(Locale.ROOT);
        if (users.existsByUsername(r.getUsername()) || users.existsByEmailIgnoreCase(email)) {
            throw new ConflictException("El username o correo ya está registrado");
        }
        checkPassword(r.getPassword());
        Users u = new Users();
        u.setFirstName(r.getFirstName().trim());
        u.setLastName(r.getLastName().trim());
        u.setUsername(r.getUsername());
        u.setEmail(email);
        u.setPasswordHash(passwords.encode(r.getPassword()));
        u.setActive(true);
        Role role = new Role(); role.setRol("ROLE_USER"); role.setUser(u);
        u.getRoles().add(role);
        return toDTO(users.saveAndFlush(u));
    }

    @Override
    public List<UsersResponseDTO> list() {
        current.requireAdmin();
        List<UsersResponseDTO> result = users.findAll(Sort.by("idUser")).stream().map(this::toDTO).toList();
        if (result.isEmpty()) throw new ResourceNotFoundException("No hay usuarios registrados");
        return result;
    }

    @Override
    public List<UsersResponseDTO> findByActive(Boolean active) {
        current.requireAdmin();
        List<UsersResponseDTO> result = users.findByActiveOrderByIdUserAsc(active).stream().map(this::toDTO).toList();
        if (result.isEmpty()) throw new ResourceNotFoundException("No hay usuarios con active=" + active);
        return result;
    }

    @Override
    public List<CountHealthProfilesByUserDTO> countProfilesByUser() {
        current.requireAdmin();
        List<CountHealthProfilesByUserDTO> result = users.getTotalHealthProfilesByUser().stream()
                .map(row -> new CountHealthProfilesByUserDTO(((Number) row[0]).longValue(),
                        (String) row[1], (String) row[2], (String) row[3], ((Number) row[4]).longValue()))
                .toList();
        if (result.isEmpty()) throw new ResourceNotFoundException("No hay usuarios para el reporte de perfiles");
        return result;
    }

    @Override
    public java.util.Optional<Users> listId(Long id) {
        current.requireAdmin();
        return users.findById(id);
    }

    @Override
    public UsersResponseDTO find(Long id) {
        current.requireOwnAccountOrAdmin(id);
        return toDTO(get(id));
    }

    @Override @Transactional
    public UsersResponseDTO update(Long id, UpdateUserDTO r) {
        current.requireOwnAccountOrAdmin(id);
        Users u = get(id);
        String email = r.getEmail().trim().toLowerCase(Locale.ROOT);
        if (users.existsByUsernameAndIdUserNot(r.getUsername(), id) || users.existsByEmailIgnoreCaseAndIdUserNot(email, id)) {
            throw new ConflictException("El username o correo ya está registrado");
        }
        u.setFirstName(r.getFirstName().trim());
        u.setLastName(r.getLastName().trim());
        u.setUsername(r.getUsername());
        u.setEmail(email);
        return toDTO(users.saveAndFlush(u));
    }

    @Override @Transactional
    public void delete(Long id) {
        current.requireOwnAccountOrAdmin(id);
        users.delete(get(id));
        users.flush(); // Una FK existente produce 409 y revierte la transacción.
    }

    @Override @Transactional
    public void changePassword(Long id, ChangePasswordDTO r) {
        Users actor = current.require();
        if (!actor.getIdUser().equals(id)) throw new AccessDeniedException("Solo puedes cambiar tu contraseña");
        if (!passwords.matches(r.getCurrentPassword(), actor.getPasswordHash())) {
            throw new BusinessRuleException("La contraseña actual es incorrecta");
        }
        checkPassword(r.getNewPassword());
        actor.setPasswordHash(passwords.encode(r.getNewPassword()));
        users.saveAndFlush(actor);
    }

    private void checkPassword(String password) {
        if (password == null || password.isBlank() || password.length() < 8 || password.getBytes(StandardCharsets.UTF_8).length > 72) {
            throw new BusinessRuleException("La contraseña debe tener al menos 8 caracteres y como máximo 72 bytes UTF-8");
        }
    }
    private Users get(Long id) {
        return users.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe un usuario con el id: " + id));
    }
    // La respuesta nunca incluye contraseña ni roles.
    private UsersResponseDTO toDTO(Users u) {
        return new UsersResponseDTO(u.getIdUser(), u.getFirstName(), u.getLastName(), u.getUsername(),
                u.getEmail(), u.getActive(), u.getCreatedAt(), u.getUpdatedAt());
    }
}

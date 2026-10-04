package pe.edu.upc.latia_lati.serviceinterfaces;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import pe.edu.upc.latia_lati.dtos.*;
public interface IUsersService {
    UsersResponseDTO register(@Valid @NotNull UsersRequestDTO request);
    List<UsersResponseDTO> list();
    List<UsersResponseDTO> findByActive(@NotNull Boolean active);
    List<CountHealthProfilesByUserDTO> countProfilesByUser();
    // Compatibilidad con RoleController, restringida a administración.
    java.util.Optional<pe.edu.upc.latia_lati.entities.Users> listId(@NotNull @Positive Long id);
    UsersResponseDTO find(@NotNull @Positive Long id);
    UsersResponseDTO update(@NotNull @Positive Long id, @Valid @NotNull UpdateUserDTO request);
    void delete(@NotNull @Positive Long id);
    void changePassword(@NotNull @Positive Long id, @Valid @NotNull ChangePasswordDTO request);
}

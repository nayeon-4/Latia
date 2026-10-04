package pe.edu.upc.latia_lati.serviceinterfaces;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.Optional;
import pe.edu.upc.latia_lati.entities.HealthProfile;
import pe.edu.upc.latia_lati.dtos.*;
public interface IHealthProfileService {
    HealthProfileDTO create(@Valid @NotNull HealthProfileRequestDTO request);
    List<HealthProfileDTO> list();
    List<HealthProfileDTO> findByBloodType(@NotNull @jakarta.validation.constraints.Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Tipo de sangre inválido") String bloodType);
    List<HealthProfileDTO> activeProfiles();
    HealthProfileDTO find(@NotNull @Positive Long id);
    HealthProfileDTO update(@NotNull @Positive Long id, @Valid @NotNull HealthProfileRequestDTO request);
    void delete(@NotNull @Positive Long id);
    // Se conserva para los otros módulos. Solo devuelve perfiles del propietario autenticado.
    Optional<HealthProfile> listId(@NotNull @Positive Long id);
}

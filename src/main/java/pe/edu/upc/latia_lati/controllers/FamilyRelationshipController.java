package pe.edu.upc.latia_lati.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import pe.edu.upc.latia_lati.entities.*;
import pe.edu.upc.latia_lati.dtos.FamilyRelationshipDTO;
import pe.edu.upc.latia_lati.exceptions.BusinessRuleException;
import pe.edu.upc.latia_lati.exceptions.ResourceNotFoundException;
import pe.edu.upc.latia_lati.serviceinterfaces.IFamilyRelationshipService;
import pe.edu.upc.latia_lati.serviceinterfaces.IHealthProfileService;

@RestController
@RequestMapping("/api/familyrelationships")
public class FamilyRelationshipController {

    private final IFamilyRelationshipService frS;
    private final IHealthProfileService hpS;

    public FamilyRelationshipController(IFamilyRelationshipService frS, IHealthProfileService hpS) {
        this.frS = frS;
        this.hpS = hpS;
    }

    // HU47: Listar relaciones familiares
    @GetMapping
    public ResponseEntity<List<FamilyRelationshipDTO>> listar() {

        // CA02: devuelve la lista vacía o con los registros existentes
        List<FamilyRelationshipDTO> lista = frS.list()
                .stream()
                .map(fr -> convertirDTO(fr))
                .toList();

        return ResponseEntity.ok(lista);
    }

    // HU46: Crear relación familiar
    @PostMapping
    public ResponseEntity<FamilyRelationshipDTO> registrar(
            @Valid @RequestBody FamilyRelationshipDTO dto) {

        // 1. Validar que ambos perfiles existan y sean distintos
        HealthProfile origen = buscarPerfil(dto.getIdOriginProfile(), "de origen");
        HealthProfile familiar = buscarPerfil(dto.getIdRelativeProfile(), "del familiar");
        validarPerfilesDistintos(origen, familiar);

        // Validar que la relación no esté registrada
        if (frS.existeRelacion(origen.getIdHealthProfile(), familiar.getIdHealthProfile())) {
            throw new BusinessRuleException(
                    "Ya existe una relación familiar entre esos perfiles");
        }

        // 2. Armar la entidad (el id lo genera la base de datos)
        FamilyRelationship fr = new FamilyRelationship();
        fr.setOriginProfile(origen);
        fr.setRelativeProfile(familiar);
        fr.setRelationshipType(dto.getRelationshipType());

        // 3. Guardar
        frS.insert(fr);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(fr.getId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(convertirDTO(fr));
    }

    // HU48: Encontrar relación familiar por Id
    @GetMapping("/{id}")
    public ResponseEntity<FamilyRelationshipDTO> buscarPorId(
            @PathVariable Long id) {

        FamilyRelationship fr = frS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe una relación familiar con el id: " + id
                        ));

        return ResponseEntity.ok(convertirDTO(fr));
    }

    // HU49: Actualizar relación familiar por Id
    @PutMapping("/{id}")
    public ResponseEntity<FamilyRelationshipDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody FamilyRelationshipDTO dto) {

        // 1. Verificar que la relación exista
        FamilyRelationship fr = frS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe una relación familiar con el id: " + id
                        ));

        // 2. Validar que ambos perfiles existan y sean distintos
        HealthProfile origen = buscarPerfil(dto.getIdOriginProfile(), "de origen");
        HealthProfile familiar = buscarPerfil(dto.getIdRelativeProfile(), "del familiar");
        validarPerfilesDistintos(origen, familiar);

        // Si cambia la pareja de perfiles, validar que no esté registrada
        boolean mismaPareja =
                fr.getOriginProfile().getIdHealthProfile().equals(origen.getIdHealthProfile())
                        && fr.getRelativeProfile().getIdHealthProfile().equals(familiar.getIdHealthProfile());

        if (!mismaPareja
                && frS.existeRelacion(origen.getIdHealthProfile(), familiar.getIdHealthProfile())) {
            throw new BusinessRuleException(
                    "Ya existe una relación familiar entre esos perfiles");
        }

        // 3. Copiar los datos editables
        fr.setOriginProfile(origen);
        fr.setRelativeProfile(familiar);
        fr.setRelationshipType(dto.getRelationshipType());

        // 4. Guardar
        frS.update(fr);

        return ResponseEntity.ok(convertirDTO(fr));
    }

    // HU50: Borrar relación familiar por Id
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        // CA02: verificar que la relación exista antes de eliminarla
        FamilyRelationship fr = frS.listId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe una relación familiar con el id: " + id
                        ));

        frS.delete(fr.getId());

        return ResponseEntity.noContent().build();
    }

    // HU73: Relaciones familiares de un perfil de salud (se mantiene la ruta original)
    @GetMapping("/perfil/{healthProfileId}")
    public ResponseEntity<List<FamilyRelationshipDTO>> listarPorPerfilDeSalud(
            @PathVariable Long healthProfileId) {

        List<FamilyRelationshipDTO> lista = frS.listarPorPerfil(healthProfileId)
                .stream()
                .map(fr -> convertirDTO(fr))
                .toList();

        return ResponseEntity.ok(lista);
    }

    private HealthProfile buscarPerfil(Long idHealthProfile, String rol) {
        return hpS.listId(idHealthProfile)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No existe el perfil de salud " + rol + " con el id: " + idHealthProfile
                        ));
    }

    private void validarPerfilesDistintos(HealthProfile origen, HealthProfile familiar) {
        if (origen.getIdHealthProfile().equals(familiar.getIdHealthProfile())) {
            throw new BusinessRuleException(
                    "El perfil de origen y el del familiar deben ser distintos");
        }
    }

    // La entidad guarda objetos HealthProfile; el DTO solo sus ids
    private FamilyRelationshipDTO convertirDTO(FamilyRelationship fr) {
        FamilyRelationshipDTO dto = new FamilyRelationshipDTO();
        dto.setIdFamilyRelationship(fr.getId());
        dto.setIdOriginProfile(fr.getOriginProfile().getIdHealthProfile());
        dto.setIdRelativeProfile(fr.getRelativeProfile().getIdHealthProfile());
        dto.setRelationshipType(fr.getRelationshipType());
        return dto;
    }
}
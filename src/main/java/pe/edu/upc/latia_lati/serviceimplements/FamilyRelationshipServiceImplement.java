package pe.edu.upc.latia_lati.serviceimplements;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.FamilyRelationship;
import pe.edu.upc.latia_lati.repositories.IFamilyRelationshipRepository;
import pe.edu.upc.latia_lati.serviceinterfaces.IFamilyRelationshipService;

@Service
public class FamilyRelationshipServiceImplement implements IFamilyRelationshipService {
    private final IFamilyRelationshipRepository frR;

    public FamilyRelationshipServiceImplement(IFamilyRelationshipRepository frR) {
        this.frR = frR;
    }

    @Override
    public void insert(FamilyRelationship familyRelationship) {
        frR.save(familyRelationship);
    }

    @Override
    public List<FamilyRelationship> list() {
        return frR.findAll();
    }

    @Override
    public void update(FamilyRelationship familyRelationship) {
        frR.save(familyRelationship);
    }

    @Override
    public void delete(Long idFamilyRelationship) {
        frR.deleteById(idFamilyRelationship);
    }

    @Override
    public Optional<FamilyRelationship> listId(Long id) {
        return frR.findById(id);
    }

    // Usado por la HU73 (relaciones de un perfil, como origen o como familiar)
    @Override
    public List<FamilyRelationship> listarPorPerfil(Long idHealthProfile) {
        return frR.findByOriginProfile_IdHealthProfileOrRelativeProfile_IdHealthProfile(
                idHealthProfile, idHealthProfile);
    }

    @Override
    public boolean existeRelacion(Long idOriginProfile, Long idRelativeProfile) {
        return frR.existsByOriginProfile_IdHealthProfileAndRelativeProfile_IdHealthProfile(
                idOriginProfile, idRelativeProfile);
    }
}
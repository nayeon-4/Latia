package pe.edu.upc.latia_lati.serviceinterfaces;

import java.util.List;
import java.util.Optional;

import pe.edu.upc.latia_lati.entities.FamilyRelationship;

public interface IFamilyRelationshipService {
    public void insert(FamilyRelationship familyRelationship);
    public List<FamilyRelationship> list();
    public void update(FamilyRelationship familyRelationship);
    public void delete(Long idFamilyRelationship);
    public Optional<FamilyRelationship> listId(Long id);
    public List<FamilyRelationship> listarPorPerfil(Long idHealthProfile);
    public boolean existeRelacion(Long idOriginProfile, Long idRelativeProfile);
}
package pe.edu.upc.latia_lati.serviceinterfaces;

import java.util.List;
import java.util.Optional;

import pe.edu.upc.latia_lati.entities.Users;

public interface IUsersService {
    public void insert(Users user);
    public List<Users> list();
    public void update(Users user);
    public void delete(Long idUser);
    public Optional<Users> listId(Long id);
}

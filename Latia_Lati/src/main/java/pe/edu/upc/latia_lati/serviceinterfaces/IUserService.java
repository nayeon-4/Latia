package pe.edu.upc.latia_lati.serviceinterfaces;

import java.util.List;
import java.util.Optional;

import pe.edu.upc.latia_lati.entities.User;

public interface IUserService {
    public void insert(User user);
    public List<User> list();
    public void update(User user);
    public void delete(Long idUser);
    public Optional<User> listId(Long id);
}

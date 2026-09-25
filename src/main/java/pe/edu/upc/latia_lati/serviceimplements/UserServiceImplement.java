package pe.edu.upc.latia_lati.serviceimplements;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.*;
import pe.edu.upc.latia_lati.repositories.*;
import pe.edu.upc.latia_lati.exceptions.*;
import pe.edu.upc.latia_lati.serviceinterfaces.IUserService;

@Service
public class UserServiceImplement implements IUserService {
    private final IUserRepository uR;


    public UserServiceImplement(IUserRepository uR) {
        this.uR = uR;
    }

    @Override
    public void insert(User user) {
        uR.save(user);
    }

    @Override
    public List<User> list() {
        return uR.findAll();
    }

    @Override
    public void update(User user) {
        uR.save(user);
    }

    @Override
    public void delete(Long idUser) {
        uR.deleteById(idUser);
    }

    @Override
    public Optional<User> listId(Long id) {
        return uR.findById(id);
    }
}

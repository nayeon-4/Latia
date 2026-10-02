package pe.edu.upc.latia_lati.serviceimplements;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.*;
import pe.edu.upc.latia_lati.repositories.*;
import pe.edu.upc.latia_lati.serviceinterfaces.IUsersService;

@Service
public class UsersServiceImplement implements IUsersService {
    private final IUsersRepository uR;


    public UsersServiceImplement(IUsersRepository uR) {
        this.uR = uR;
    }

    @Override
    public void insert(Users user) {
        uR.save(user);
    }

    @Override
    public List<Users> list() {
        return uR.findAll();
    }

    @Override
    public void update(Users user) {
        uR.save(user);
    }

    @Override
    public void delete(Long idUser) {
        uR.deleteById(idUser);
    }

    @Override
    public Optional<Users> listId(Long id) {
        return uR.findById(id);
    }
}

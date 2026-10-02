package pe.edu.upc.latia_lati.serviceimplements;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.edu.upc.latia_lati.entities.Users;
import pe.edu.upc.latia_lati.repositories.IUsersRepository;

import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final IUsersRepository usersRepository;

    public JwtUserDetailsService(IUsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "Usuario no encontrado: " + username
                        )
                );

        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getRol()))
                .map(authority -> (GrantedAuthority) authority)
                .toList();

        return User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(authorities)
                .disabled(!Boolean.TRUE.equals(user.getActive()))
                .build();
    }
}
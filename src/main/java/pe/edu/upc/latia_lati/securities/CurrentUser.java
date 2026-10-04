package pe.edu.upc.latia_lati.securities;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import pe.edu.upc.latia_lati.entities.Users;
import pe.edu.upc.latia_lati.repositories.IUsersRepository;

/** Comprueba la cuenta vigente, no solo los roles históricos de un JWT. */
@Component
public class CurrentUser {
    private final IUsersRepository users;
    public CurrentUser(IUsersRepository users) { this.users = users; }

    public Users require() {
        org.springframework.security.core.Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof JwtAuthenticationToken) || !auth.isAuthenticated()) {
            throw new BadCredentialsException("Inicia sesión para continuar");
        }
        JwtAuthenticationToken jwt = (JwtAuthenticationToken) auth;
        Object claim = jwt.getToken().getClaim("userId");
        Long id;
        try { id = Long.valueOf(String.valueOf(claim)); }
        catch (NumberFormatException ex) { throw new BadCredentialsException("Vuelve a iniciar sesión"); }
        Users user = users.findById(id).orElseThrow(() -> new BadCredentialsException("La cuenta ya no está disponible"));
        if (!Boolean.TRUE.equals(user.getActive()) || !user.getUsername().equals(jwt.getName())) {
            throw new BadCredentialsException("Vuelve a iniciar sesión con una cuenta activa");
        }
        return user;
    }

    public boolean isAdmin(Users user) {
        return user.getRoles().stream().anyMatch(r -> "ROLE_ADMIN".equals(r.getRol()) || "ADMIN".equals(r.getRol()));
    }

    public Users requireAdmin() {
        Users actor = require();
        if (!isAdmin(actor)) throw new AccessDeniedException("Esta operación requiere administrador");
        return actor;
    }

    public void requireOwnAccountOrAdmin(Long id) {
        Users actor = require();
        if (!actor.getIdUser().equals(id) && !isAdmin(actor)) {
            throw new AccessDeniedException("No puedes gestionar esta cuenta");
        }
    }
}

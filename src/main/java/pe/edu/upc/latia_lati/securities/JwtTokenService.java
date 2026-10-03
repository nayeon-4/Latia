package pe.edu.upc.latia_lati.securities;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtTokenService {
    private final JwtEncoder jwtEncoder;
    private final pe.edu.upc.latia_lati.repositories.IUsersRepository users;

    private static final long TOKEN_VALIDITY = 5 * 60 * 60;

    public JwtTokenService(JwtEncoder jwtEncoder, pe.edu.upc.latia_lati.repositories.IUsersRepository users) {
        this.users = users;
        this.jwtEncoder = jwtEncoder;
    }

    public String generateToken(UserDetails userDetails) {

        Instant now = Instant.now();

        String roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(userDetails.getUsername())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(TOKEN_VALIDITY))
                .claim("roles", roles)
                .claim("userId", users.findByUsername(userDetails.getUsername()).orElseThrow().getIdUser())
                .build();

        JwsHeader header = JwsHeader
                .with(MacAlgorithm.HS512)
                .build();

        return jwtEncoder
                .encode(
                        JwtEncoderParameters.from(
                                header,
                                claims
                        )
                )
                .getTokenValue();
    }
}



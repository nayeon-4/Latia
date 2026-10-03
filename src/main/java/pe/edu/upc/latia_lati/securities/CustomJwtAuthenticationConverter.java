package pe.edu.upc.latia_lati.securities;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

        @Override
        public AbstractAuthenticationToken convert(Jwt jwt) {

            String roles = jwt.getClaimAsString("roles");

            List<SimpleGrantedAuthority> authorities;

            if (roles == null || roles.isBlank()) {

                authorities = Collections.emptyList();

            } else {

                authorities = Arrays.stream(roles.split(","))
                        .map(String::trim)
                        .filter(role -> !role.isBlank())
                        .map(SimpleGrantedAuthority::new)
                        .toList();
            }

            return new JwtAuthenticationToken(
                    jwt,
                    authorities,
                    jwt.getSubject()
            );
        }
    }

package pe.edu.upc.latia_lati.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.edu.upc.latia_lati.dtos.LoginRequestDTO;
import pe.edu.upc.latia_lati.dtos.LoginResponseDTO;
import pe.edu.upc.latia_lati.securities.JwtTokenService;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final AuthenticationManager authenticationManager;

    private final JwtTokenService jwtTokenService;

    public LoginController(
            AuthenticationManager authenticationManager,
            JwtTokenService jwtTokenService) {

        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
    }

    @PostMapping
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String token =
                jwtTokenService.generateToken(userDetails);

        return ResponseEntity.ok(
                new LoginResponseDTO(
                        token,
                        userDetails.getUsername()
                )
        );
    }
}

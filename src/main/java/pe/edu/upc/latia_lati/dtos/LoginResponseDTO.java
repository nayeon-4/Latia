package pe.edu.upc.latia_lati.dtos;

public class LoginResponseDTO {
    private String token;

    private String username;

    public LoginResponseDTO(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }
}

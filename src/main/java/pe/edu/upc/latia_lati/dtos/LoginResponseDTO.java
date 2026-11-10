package pe.edu.upc.latia_lati.dtos;

public class LoginResponseDTO {
    private String token;

    private String email;

    public LoginResponseDTO(String token, String email) {
        this.token = token;
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public String getEmail() { return email; }
}

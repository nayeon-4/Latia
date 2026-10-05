package pe.edu.upc.latia_lati.dtos;

// Resultado de la consulta nativa de conteo de perfiles por usuario.
public class CountHealthProfilesByUserDTO {
    private Long idUser;
    private String firstName;
    private String lastName;
    private String username;
    private Long totalProfiles;

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTotalProfiles() {
        return totalProfiles;
    }

    public void setTotalProfiles(Long totalProfiles) {
        this.totalProfiles = totalProfiles;
    }
}

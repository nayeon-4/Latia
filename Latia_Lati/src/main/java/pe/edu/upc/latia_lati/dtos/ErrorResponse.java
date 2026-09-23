package pe.edu.upc.latia_lati.dtos;

public class ErrorResponse {
    private int status;
    private String message;
    private String path;

    public ErrorResponse(int status, String message, String path) {
        this.status = status; this.message = message; this.path = path;
    }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}

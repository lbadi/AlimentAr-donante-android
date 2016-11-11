package proyectoalimentar.alimentardonanteapp.model;

public class AuthenticatedUser extends User {

    private String accessToken;
    private transient String email;

    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

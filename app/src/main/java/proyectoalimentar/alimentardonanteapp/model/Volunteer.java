package proyectoalimentar.alimentardonanteapp.model;

public class Volunteer {

    String email;
    String name;
    String username;
    Avatar avatar;
    Double averageQualification;

    public Volunteer(String email, String name, String userName, Avatar avatar, Double averageQualification) {
        this.email = email;
        this.name = name;
        this.username = userName;
        this.avatar = avatar;
        this.averageQualification = averageQualification;
    }

    public String getEmail() {
        return email;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public Double getAverageQualification() {
        return averageQualification;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }
}

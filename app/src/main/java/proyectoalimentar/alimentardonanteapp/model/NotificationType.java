package proyectoalimentar.alimentardonanteapp.model;

public enum  NotificationType {
    DONATION_ACTIVATED("donation_activated"),
    DONATION_EXPIRED("donation_expired"),
    ACTIVATION_TIME_PASSED("activation_time_passed"),;

    private String name;

    NotificationType(String name){
        this.name = name;
    }
    public String getName() {
        return name;
    }
}
package proyectoalimentar.alimentardonanteapp.model;

import proyectoalimentar.alimentardonanteapp.R;

public enum  NotificationType {
    DONATION_ACTIVATED("donation_activated", R.string.notifiaction_activated_title),
    DONATION_EXPIRED("donation_expired", R.string.notifiaction_expired_title),
    ACTIVATION_TIME_PASSED("activation_time_passed", R.string.notifiaction_time_passed_title),
    DONATION_DEACTIVATED("donation_deactivated", R.string.notifiaction_donation_deavtivated_title);

    private String name;
    private int titleResource;

    NotificationType(String name, int titleResource){
        this.name = name;
        this.titleResource = titleResource;
    }
    public String getName() {
        return name;
    }

    public int getTitleResource(){
        return titleResource;
    }

    public static NotificationType fromString(String name) {
        if (name != null) {
            for (NotificationType n : NotificationType.values()) {
                if (name.equalsIgnoreCase(n.getName())) {
                    return n;
                }
            }
        }
        throw new IllegalArgumentException("No Notification type found with name " + name);
    }
}
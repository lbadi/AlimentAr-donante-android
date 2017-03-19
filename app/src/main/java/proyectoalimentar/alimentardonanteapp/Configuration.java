package proyectoalimentar.alimentardonanteapp;

import com.google.android.gms.maps.model.LatLng;

public class Configuration {

    public static final String ACCESS_TOKEN = "LOGGED_IN_ACCESS_TOKEN";
    public static final String EMAIL = "EMAIL";
    public static final String USER_INFORMATION = "USER_INFORMATION";
    public static final String ENDPOINT = "http://alimentar-stage.herokuapp.com/api/v1/";
//    public static final String ENDPOINT = "http://1a84f3c6.ngrok.io/api/v1/";

    //    Notifications configuration
    public static final String SENT_TOKEN_TO_SERVER = "SENT_TOKEN_TO_SERVER";
    public static final String REGISTRATION_COMPLETE = "REGISTRATION_COMPLETE";
    public static final String LAST_DONATIONS = "LAST_DONATIONS";
    public static final String TOKEN = "TOKEN";
    public static final String DONATION = "DONATION";
    public static final String VOLUNTEER_NAME = "VOLUNTEER_NAME";
    public static final String NOTIFICATION_TYPE = "NOTIFICATION_TYPE";

    //Pick Place
    public static final LatLng NORTHEAST_BORDER = new LatLng(-34.542276,-58.361092);
    public static final LatLng SOUTHWEST_BORDER = new LatLng(-34.666050,-58.520393);

}

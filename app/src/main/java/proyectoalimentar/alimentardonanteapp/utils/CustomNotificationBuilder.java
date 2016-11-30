package proyectoalimentar.alimentardonanteapp.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.model.NotificationType;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;

/**
 * Created by leonelbadi on 26/11/16.
 */
public class CustomNotificationBuilder {

    private static final String NOTIFICATION_TITLE = "Titulo de notification";

    public static Notification build(NotificationType notificationType, String message, Context context){
        switch (notificationType){
            case ACTIVATION_TIME_PASSED:
            break;
            case DONATION_ACTIVATED:
                Intent intent = new Intent(context, DrawerActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                        PendingIntent.FLAG_ONE_SHOT);

                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                return new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.img_default)
                        .setContentTitle(context.getString(notificationType.getTitleResource()))
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent).build();
            case DONATION_EXPIRED:
            break;

        }

        return null;


    }
}

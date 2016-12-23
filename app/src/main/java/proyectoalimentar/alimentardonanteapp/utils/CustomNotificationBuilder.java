package proyectoalimentar.alimentardonanteapp.utils;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import proyectoalimentar.alimentardonanteapp.Configuration;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.model.NotificationType;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;

/**
 * Builder of the notifications.
 */
public class CustomNotificationBuilder {

    public static Notification build(NotificationType notificationType, String message, Context context){
        return build(notificationType,message,context,null);
    }

    public static Notification build(NotificationType notificationType, String message, Context context, String donationId){
        switch (notificationType){
            case ACTIVATION_TIME_PASSED:
                return getActivationTimePassedNotification(context.getString(notificationType.getTitleResource()),
                        message,
                        context,
                        donationId);
            case DONATION_ACTIVATED:
                return getSimpleMessageNotification(
                        context.getString(notificationType.getTitleResource())
                        ,message
                        ,context);
            case DONATION_EXPIRED:
                break;
            case DONATION_DEACTIVATED:
                return getSimpleMessageNotification(
                        context.getString(notificationType.getTitleResource())
                        ,message
                        ,context);

        }

        return null;


    }

    private static Notification getSimpleMessageNotification(String title, String message, Context context){
        Intent intent = new Intent(context, DrawerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.img_default)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent).build();
    }

    private static Notification getActivationTimePassedNotification(String title, String message,
                                                                    Context context, String donationId){
        Intent intent = new Intent(context, DrawerActivity.class);
        //Put extra information
        intent.putExtra(Configuration.NOTIFICATION_TYPE,NotificationType.ACTIVATION_TIME_PASSED);
        intent.putExtra(Configuration.DONATION, donationId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        return new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.img_default)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent).build();
    }
}

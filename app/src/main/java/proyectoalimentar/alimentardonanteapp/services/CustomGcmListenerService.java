package proyectoalimentar.alimentardonanteapp.services;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import java.util.Date;

import proyectoalimentar.alimentardonanteapp.model.NotificationType;
import proyectoalimentar.alimentardonanteapp.utils.CustomNotificationBuilder;


public class CustomGcmListenerService extends GcmListenerService{

    private static final String TAG = "MyGcmListenerService";
    private static final String NOTIFICATION_TITLE = "Titulo de notification";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.d(TAG, "Keyset: " + data.keySet());
        String message = data.getString("message_body");
        String notificationType = data.getString("n_type");
        String donationId = data.getString("user_id");
        String userName = data.getString("user_name");

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        sendNotification(message,NotificationType.fromString(notificationType), donationId, userName);
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, NotificationType notificationType, String donationId, String userName) {

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 0;
        if(notificationType.equals(NotificationType.ACTIVATION_TIME_PASSED)){
            notificationId = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        }
        notificationManager.notify(notificationId /* ID of notification */,CustomNotificationBuilder.build(notificationType,message,this, donationId,userName));
    }
}

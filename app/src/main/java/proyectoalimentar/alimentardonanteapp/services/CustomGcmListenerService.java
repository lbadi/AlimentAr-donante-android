package proyectoalimentar.alimentardonanteapp.services;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

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
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + message);
        sendNotification(message,NotificationType.valueOf(notificationType));
    }

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message, NotificationType notificationType) {

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */,CustomNotificationBuilder.build(notificationType,message,this));
    }
}

package proyectoalimentar.alimentardonanteapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import javax.inject.Inject;

import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.Configuration;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.network.NotificationService;
import proyectoalimentar.alimentardonanteapp.repository.UserRepository;
import proyectoalimentar.alimentardonanteapp.utils.StorageUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String DEVICE_TYPE = "android";
    private static final String[] TOPICS = {"global"};

    @Inject
    NotificationService notificationService;

    public RegistrationIntentService() {
        super(TAG);
        AlimentarApp.inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            Log.i(TAG,"Registration started");
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);
            sendRegistrationTokenToServer(token);


            // Notify UI that registration has completed.
            Intent registrationComplete = new Intent(Configuration.REGISTRATION_COMPLETE);
            LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
        }catch (Exception e){
            Log.e(TAG, "GCM Registration Token: " + "Registration Error", e);
            storeSendStatus(false);
        }
    }


    private void sendRegistrationTokenToServer(final String token) throws Exception{
        notificationService.registerToken(token, DEVICE_TYPE).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(!response.isSuccessful()) {
                    storeSendStatus(false);
                    Log.i(TAG, "GCM Registration Token: " + "Registration error");
                    return;
                }else{
                    Log.i(TAG, "GCM Registration Token: " + "Registration complete");
                    storeSendStatus(true);
                    UserRepository.storeToken(token);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.i(TAG, "GCM Registration Token: " + "Registration error");
                storeSendStatus(false);
            }
        });
    }

    private void storeSendStatus(boolean send){
        UserRepository.setTokenPresent(send);
    }

}

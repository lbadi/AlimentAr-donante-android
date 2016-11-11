package proyectoalimentar.alimentardonanteapp.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import proyectoalimentar.alimentardonanteapp.Configuration;
import proyectoalimentar.alimentardonanteapp.model.Donation;
import proyectoalimentar.alimentardonanteapp.utils.StorageUtils;


public class DonationWatcherService extends IntentService{

    private static final String TAG = "DonationWatcherService";

    public DonationWatcherService(){
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Service running");
        Type listDonationsType = new TypeToken<ArrayList<Donation>>(){}.getType();

        List donations = StorageUtils.getObjectFromSharedPreferences(Configuration.LAST_DONATIONS, listDonationsType);
        if(donations == null || donations.isEmpty()){
            return;
        }
        Log.e(TAG, "Watcher have information");
    }

}

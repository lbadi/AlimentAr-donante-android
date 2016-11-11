package proyectoalimentar.alimentardonanteapp.ui.drawer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;

import com.annimon.stream.Stream;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.Configuration;
import proyectoalimentar.alimentardonanteapp.services.DonationWatcherService;
import proyectoalimentar.alimentardonanteapp.services.RegistrationIntentService;
import proyectoalimentar.alimentardonanteapp.ui.donations.NewDonationFragment;
import proyectoalimentar.alimentardonanteapp.utils.StorageUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import android.widget.Toast;

import proyectoalimentar.alimentardonanteapp.R;

public class DrawerActivity extends AppCompatActivity {

    private static final DrawerItem DEFAULT_ITEM = DrawerItem.DONATIONS;
    private static final String LAST_SELECTED_ITEM = "LAST_SELECTED_ITEM";
    private static final int REQUEST_CODE = 0;


    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    private Map<DrawerItem, DrawerItemContainer> drawerItems;
    private DrawerItem selectedItem;

    private boolean isReceiverRegistered;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_activity);
        ButterKnife.bind(this);

        //Register GCM Token
        registerToken();
        //Register Donations watcher
        registerDonationWatcher(this);

        drawerItems = new HashMap<>();
        Stream.of(DrawerItem.values())
                .forEach(item ->
                        drawerItems.put(item, new DrawerItemContainer(DrawerActivity.this, item)));
        setListeners();

        //Try to retrieve the last selected item (When the orientation is changed the activity is destroyed)
        if(savedInstanceState != null) {
            selectedItem = (DrawerItem) savedInstanceState.getSerializable(LAST_SELECTED_ITEM);
        }
        if(selectedItem != null){
            openDrawerItem(selectedItem);
        }else{
            openDrawerItem(DEFAULT_ITEM);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current selected item
        savedInstanceState.putSerializable(LAST_SELECTED_ITEM, selectedItem);
        super.onSaveInstanceState(savedInstanceState);
    }

    public void setListeners(){
        ((NewDonationFragment)drawerItems.get(DrawerItem.NEW_DONATION).getFragment()).
                setOnDonationCreated(()-> onSuccessfullyCreateDonation());
    }


    public void openDrawerItem(DrawerItem drawerItem) {
        selectedItem = drawerItem;
        hideDrawer();
        if(drawerItem.equals(DrawerItem.SIGN_OUT)){
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(drawerItems.get(drawerItem).getFragment(),drawerItem.toString())
                    .commit();
            return;
        }
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, drawerItems.get(drawerItem).getFragment())
                .commit();
    }

    public void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            hideDrawer();
        } else {
            showDrawer();
        }
    }

    private void showDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    private void hideDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (selectedItem == DrawerItem.DONATIONS) {
            super.onBackPressed();
        } else {
            openDrawerItem(DrawerItem.DONATIONS);
        }
    }

    public void onSuccessfullyCreateDonation(){
        onBackPressed();
        Toast.makeText(this,R.string.donation_created, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

//    private void registerReceiver(){
//        if(!isReceiverRegistered) {
//            LocalBroadcastManager.getInstance(this).registerReceiver(RegistrationIntentService,
//                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
//            isReceiverRegistered = true;
//        }
//    }

    private void registerToken(){
        if(!StorageUtils.getBooleanFromSharedPreferences(Configuration.SENT_TOKEN_TO_SERVER, false)){
            //Only register token if is not registered yet.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    private void registerDonationWatcher(Context context) {
        Intent i = new Intent(context, DonationWatcherService.class);
        PendingIntent sender = PendingIntent.getService(context,REQUEST_CODE,i,0);

        // We want the alarm to go off 3 seconds from now.
        long firstTime = SystemClock.elapsedRealtime();
        firstTime += 3 * 1000;//start 3 seconds after first register.

        // Schedule the alarm!
        AlarmManager am = (AlarmManager) context
                .getSystemService(ALARM_SERVICE);
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstTime,
                6000, sender);
    }

}

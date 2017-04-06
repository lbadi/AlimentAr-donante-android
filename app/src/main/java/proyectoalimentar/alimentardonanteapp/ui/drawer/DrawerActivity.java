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
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.Configuration;
import proyectoalimentar.alimentardonanteapp.model.Donator;
import proyectoalimentar.alimentardonanteapp.model.NotificationType;
import proyectoalimentar.alimentardonanteapp.repository.DonationRepository;
import proyectoalimentar.alimentardonanteapp.repository.RepoCallBack;
import proyectoalimentar.alimentardonanteapp.repository.UserRepository;
import proyectoalimentar.alimentardonanteapp.services.DonationWatcherService;
import proyectoalimentar.alimentardonanteapp.services.RegistrationIntentService;
import proyectoalimentar.alimentardonanteapp.ui.donations.ActivatedQuestionView;
import proyectoalimentar.alimentardonanteapp.ui.donations.NewDonationFragment;
import proyectoalimentar.alimentardonanteapp.ui.donations.QualifyVolunteerView;
import proyectoalimentar.alimentardonanteapp.utils.StorageUtils;
import proyectoalimentar.alimentardonanteapp.utils.UserStorage;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import proyectoalimentar.alimentardonanteapp.R;

public class DrawerActivity extends AppCompatActivity {

    private static final DrawerItem DEFAULT_ITEM = DrawerItem.DONATIONS;
    private static final String LAST_SELECTED_ITEM = "LAST_SELECTED_ITEM";
    private static final int REQUEST_CODE = 0;


    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.profile_image)
    SimpleDraweeView profileImage;
    @BindView(R.id.activated_question_view)
    ActivatedQuestionView activatedQuestionView;
    @BindView(R.id.qualify_volunteer_view)
    QualifyVolunteerView qualifyVolunteerView;

    @Inject
    UserStorage userStorage;
    @Inject
    DonationRepository donationRepository;
    @Inject
    UserRepository userRepository;

    private Map<DrawerItem, DrawerItemContainer> drawerItems;
    private DrawerItem selectedItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlimentarApp.inject(this);
        setContentView(R.layout.drawer_activity);
        ButterKnife.bind(this);
        init(savedInstanceState);
    }

    private void init(@Nullable Bundle savedInstanceState){
        //Register GCM Token
        registerToken();
        //Put user information in nav-bar Header
        fetchDonatorInformation();
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
        reactToIntent();
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

    private void registerToken(){
        if(!StorageUtils.getBooleanFromSharedPreferences(Configuration.SENT_TOKEN_TO_SERVER, false)){
            //Only register token if is not registered yet.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }


    public void fetchDonatorInformation(){
        userRepository.getMyInformation(new RepoCallBack<Donator>() {
            @Override
            public void onSuccess(Donator donator) {
                refreshDonatorInformation(donator);
            }
            @Override
            public void onError(String error) {

            }
        });
    }

    private void refreshDonatorInformation(Donator donator){
        if(donator.getName() != null && !donator.getName().isEmpty()){
            name.setText(donator.getName());
        }
        if(donator.getAddress() != null && !donator.getAddress().isEmpty()) {
            address.setText(donator.getAddress());
        }
        if(donator.getAvatar() != null && donator.getAvatar().getThumb() != null){
            profileImage.setImageURI(donator.getAvatar().getThumb());
        }
    }

    private void reactToIntent(){
        Intent intent = getIntent();
        NotificationType notificationType;
        if(intent == null){
            return;
        }
        notificationType = getNotificationType(intent);
        if( notificationType == null){
            return;
        }
        switch (notificationType){
            case ACTIVATION_TIME_PASSED:
                reactToActivationTimePassed(intent);
                break;
            default:
        }

    }

    private void reactToActivationTimePassed(Intent intent){
        String donationId = intent.getStringExtra(Configuration.DONATION);
        String userName = intent.getStringExtra(Configuration.VOLUNTEER_NAME);

        //This shouldn't happen except there was an error on the notification system.
        if(donationId ==null || donationId.isEmpty()){
            return;
        }
        activatedQuestionView.setInformation(donationId,userName);
        activatedQuestionView.setOnResponseCallback(confirmActivation -> {
            //Here we show the qualify view.
            if(confirmActivation) {
                qualifyVolunteerView.setInformation(donationId,userName);
                qualifyVolunteerView.setVisibility(View.VISIBLE);
            }
        });
        activatedQuestionView.setVisibility(View.VISIBLE);

    }

    private NotificationType getNotificationType(Intent intent){
        return (NotificationType) intent.getSerializableExtra(Configuration.NOTIFICATION_TYPE);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
    }

}

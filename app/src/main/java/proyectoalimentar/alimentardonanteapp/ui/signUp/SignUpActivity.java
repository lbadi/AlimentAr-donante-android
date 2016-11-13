package proyectoalimentar.alimentardonanteapp.ui.signUp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;

import javax.inject.Inject;

import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.repository.RepoCallBack;
import proyectoalimentar.alimentardonanteapp.repository.UserRepository;
import proyectoalimentar.alimentardonanteapp.model.AuthenticatedUser;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;
import proyectoalimentar.alimentardonanteapp.utils.UserStorage;

public class SignUpActivity extends AppCompatActivity {

    String email;
    String password;


    FragmentManager fragmentManager = getSupportFragmentManager();

    MainDataSignUpFragment mainDataFragment;

    @Inject
    UserStorage userStorage;
    @Inject
    UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlimentarApp.inject(this);

        if (userStorage.isUserLogged()) {
            startActivity(new Intent(this, DrawerActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        initMainDataFragment();
    }

    private void initMainDataFragment(){
        //Create and initialize main data fragment.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mainDataFragment = new MainDataSignUpFragment();
        mainDataFragment.setOnRegisterAttemptListener((email, password) -> saveDataAndInitAdditionalDataFragment(email,password));
        fragmentTransaction.add(R.id.fragment_container,mainDataFragment);
        fragmentTransaction.commit();
    }

    public void saveDataAndInitAdditionalDataFragment(String email, String password){
        this.email = email;
        this.password = password;
        initAdditionalDataFrament();
    }

    private void initAdditionalDataFrament(){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(mainDataFragment!=null){
            fragmentTransaction.remove(mainDataFragment);
        }
        AditionalDataSignUpFragment aditionalDataSignUpFragment = new AditionalDataSignUpFragment();
        aditionalDataSignUpFragment.setOnRegisterFinishAttemptListener((name,direction) -> attemptRegistration(name,direction));
        fragmentTransaction.add(R.id.fragment_container,aditionalDataSignUpFragment);
        fragmentTransaction.commit();
    }

    private void attemptRegistration(String name, Place direction){
//        Call register service
//            progressBar.setVisibility(View.VISIBLE);
        userRepository.signUp(email, password, name, direction.getAddress().toString(),
                new RepoCallBack<AuthenticatedUser>() {
            @Override
            public void onSuccess(AuthenticatedUser value) {
                startActivity(new Intent(SignUpActivity.this, DrawerActivity.class));
                finish();
            }

            @Override
            public void onError(String error) {
                showRegisterError();
            }
        });
    }

    private void showRegisterError(){
        Toast.makeText(this, R.string.error_signup, Toast.LENGTH_SHORT).show();
    }

//    private void selectLocation(){
//        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
//        try {
//            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        }
//    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PLACE_PICKER_REQUEST) {
//            if (resultCode == RESULT_OK) {
//                pickedPlace = PlacePicker.getPlace(data, this);
//                pickAddressText.setText(pickedPlace.getAddress());
//                String toastMsg = String.format("Place: %s", pickedPlace.getName());
//                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
//            }
//        }
//    }

}

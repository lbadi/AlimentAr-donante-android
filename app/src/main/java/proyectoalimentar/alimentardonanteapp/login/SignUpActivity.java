package proyectoalimentar.alimentardonanteapp.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.donations.DonationsActivity;
import proyectoalimentar.alimentardonanteapp.model.AuthenticatedUser;
import proyectoalimentar.alimentardonanteapp.network.LoginService;
import proyectoalimentar.alimentardonanteapp.network.RetrofitServices;
import proyectoalimentar.alimentardonanteapp.utils.UserStorage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    private static final int PLACE_PICKER_REQUEST = 1;


    @BindView(R.id.email)
    EditText emailField;
    @BindView(R.id.password)
    EditText passwordField;
    @BindView(R.id.password_confirmation)
    EditText passwordConfirmationField;
    @BindView(R.id.nameField)
    EditText nameField;
    @BindView(R.id.sign_up_progress)
    ProgressBar progressBar;
    @BindView(R.id.sign_up_button)
    Button signUpButton;
    @BindView(R.id.pick_address_button)
    Button pickAddressButton;
    @BindView(R.id.pick_addres_text)
    TextView pickAddressText;


    LoginService loginService;

    @Inject
    UserStorage userStorage;

    Place pickedPlace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlimentarApp.inject(this);

        if (userStorage.isUserLogged()) {
            startActivity(new Intent(this, DonationsActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_sign_up);

        ButterKnife.bind(this);

        loginService = RetrofitServices.getService(LoginService.class);

        signUpButton.setOnClickListener(view -> attemptRegister());
        pickAddressButton.setOnClickListener(view -> selectLocation());
    }

    private void attemptRegister(){

        View focusView = null;
        boolean cancel = false;
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String passwordConfirmation = passwordConfirmationField.getText().toString();
        String name = nameField.getText().toString();


        //Sign up request
        if(!isEmailValid(email)){
            focusView = emailField;
            emailField.setError(getResources().getString(R.string.error_invalid_email));
            cancel = true;
        }
        if(!isPasswordValid(password)){
            focusView = passwordField;
            passwordField.setError(getResources().getString(R.string.error_invalid_password));
            cancel = true;
        }
        if(!isNameValid(name)){
            focusView = nameField;
            nameField.setError(getResources().getString(R.string.error_invalid_name));
            cancel = true;
        }
        if(!password.equals(passwordConfirmation)){
            focusView=passwordConfirmationField;
            passwordConfirmationField.setError(getResources().getString(R.string.error_password_mismatch));
            cancel = true;
        }
        if(pickedPlace == null){
            focusView=pickAddressButton;
            pickAddressButton.setError(getResources().getString(R.string.error_pick_address));
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
        }else{
            //Call register service
            progressBar.setVisibility(View.VISIBLE);
            loginService.signUp(email,password,passwordConfirmation,name,pickedPlace.getAddress().toString())
                    .enqueue(new Callback<AuthenticatedUser>() {
                @Override
                public void onResponse(Call<AuthenticatedUser> call, Response<AuthenticatedUser> response) {
                    progressBar.setVisibility(View.GONE);
                    if(response.isSuccessful()){
                        userStorage.login(response.body());
                        startActivity(new Intent(SignUpActivity.this, DonationsActivity.class));
                        finish();
                    }else{
                        showRegisterError();
                    }
                }

                @Override
                public void onFailure(Call<AuthenticatedUser> call, Throwable t) {
                    progressBar.setVisibility(View.GONE);
                    showRegisterError();
                }
            });
        }


    }

    private void selectLocation(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                pickedPlace = PlacePicker.getPlace(data, this);
                pickAddressText.setText(pickedPlace.getAddress());
                String toastMsg = String.format("Place: %s", pickedPlace.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void showRegisterError(){
        Toast.makeText(SignUpActivity.this, R.string.error_signup, Toast.LENGTH_SHORT).show();
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    private boolean isNameValid(String name){
        return !name.isEmpty() && name.length() < 255;
    }
}

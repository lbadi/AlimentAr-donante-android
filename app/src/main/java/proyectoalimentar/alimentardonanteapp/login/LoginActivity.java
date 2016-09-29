package proyectoalimentar.alimentardonanteapp.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

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

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.email)
    EditText emailField;
    @BindView(R.id.password)
    EditText passwordField;
    @BindView(R.id.login_form)
    View mLoginFormView;
    @BindView(R.id.email_sign_in_button)
    Button signIn;
    @BindView(R.id.sign_up_button)
    Button signUp;
    @BindView(R.id.sign_in_progress)
    ProgressBar progressBar;

    LoginService loginService;

    @Inject
    UserStorage userStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlimentarApp.inject(this);

        if (userStorage.isUserLogged()) {
            startActivity(new Intent(this, DonationsActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        loginService = RetrofitServices.getService(LoginService.class);

        signIn.setOnClickListener(view -> attemptLogin());
        signUp.setOnClickListener(view -> attemptRegister());

    }

    /**
     * Make an intent start register activity
     */
    private void attemptRegister(){
        startActivity(new Intent(this, SignUpActivity.class));
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        emailField.setError(null);
        passwordField.setError(null);

        // Store values at the time of the login attempt.
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();

        View focusView = null;
        boolean cancel = false;

        if(!isEmailValid(email)){
            focusView = emailField;
            emailField.setError(getResources().getString(R.string.error_invalid_email));
            cancel = true;
        }

        if(cancel){
            //There was an error, dont attempt loggin
            focusView.requestFocus();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            loginService.login(email,password).enqueue(new Callback<AuthenticatedUser>() {
                @Override
                public void onResponse(Call<AuthenticatedUser> call, Response<AuthenticatedUser> response) {
                    progressBar.setVisibility(View.INVISIBLE);
                    if (response.isSuccessful()) {
                        userStorage.login(response.body());
                        startActivity(new Intent(LoginActivity.this, DonationsActivity.class));
                        finish();
                    } else {
                        showLoginError();
                    }
                }

                @Override
                public void onFailure(Call<AuthenticatedUser> call, Throwable t) {
                    progressBar.setVisibility(View.INVISIBLE);
                    showLoginError();
                }
            });

        }
    }
    private void showLoginError(){
        Toast.makeText(LoginActivity.this, R.string.error_login, Toast.LENGTH_SHORT).show();

    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }


}


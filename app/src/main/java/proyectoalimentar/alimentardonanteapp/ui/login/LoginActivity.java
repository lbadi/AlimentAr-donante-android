package proyectoalimentar.alimentardonanteapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.repository.RepoCallBack;
import proyectoalimentar.alimentardonanteapp.repository.UserRepository;
import proyectoalimentar.alimentardonanteapp.model.AuthenticatedUser;
import proyectoalimentar.alimentardonanteapp.network.LoginService;
import proyectoalimentar.alimentardonanteapp.network.RetrofitServices;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;
import proyectoalimentar.alimentardonanteapp.ui.signUp.SignUpActivity;
import proyectoalimentar.alimentardonanteapp.ui.terms_and_condition.TermsAndConditionActivity;
import proyectoalimentar.alimentardonanteapp.utils.UserStorage;

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

    @BindColor(R.color.light_gray) int disabledColor;
    @BindColor(R.color.colorPrimary) int enabledColorSignIn;
    @BindColor(R.color.colorPrimaryDark) int enabledColorSignUp;


    LoginService loginService;

    @Inject
    UserRepository userRepository;

    @Inject
    UserStorage userStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlimentarApp.inject(this);
        if (userStorage.isUserLogged()) {
            startActivity(new Intent(this, DrawerActivity.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        loginService = RetrofitServices.getService(LoginService.class);

        enableButton(signIn,false, disabledColor); //The user must accept terms first
        enableButton(signUp,false, disabledColor);
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
            userRepository.login(email, password, new RepoCallBack<AuthenticatedUser>() {
                @Override
                public void onSuccess(AuthenticatedUser value) {
                    progressBar.setVisibility(View.INVISIBLE);
                    startActivity(new Intent(LoginActivity.this, DrawerActivity.class));
                    finish();
                }

                @Override
                public void onError(String error) {
                    progressBar.setVisibility(View.INVISIBLE);
                    showLoginError();
                }
            });
//            loginService.login(email,password).enqueue(new Callback<AuthenticatedUser>() {
//                @Override
//                public void onResponse(Call<AuthenticatedUser> call, Response<AuthenticatedUser> response) {
//                    progressBar.setVisibility(View.INVISIBLE);
//                    if (response.isSuccessful()) {
//                        response.body().setEmail(email); //Set email before saving in shared preferences
//                        userStorage.login(response.body());
//                        startActivity(new Intent(LoginActivity.this, DrawerActivity.class));
//                        finish();
//                    } else {
//                        showLoginError();
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<AuthenticatedUser> call, Throwable t) {
//                    progressBar.setVisibility(View.INVISIBLE);
//                    showLoginError();
//                }
//            });

        }
    }
    private void showLoginError(){
        Toast.makeText(LoginActivity.this, R.string.error_login, Toast.LENGTH_SHORT).show();

    }
    private boolean isEmailValid(String email) {
        return email.contains("@");
    }


    @OnCheckedChanged(R.id.accept_terms)
    void onTermsAcceptedChanged(CompoundButton button, boolean checked) {
        enableButton(signIn, checked, checked ? enabledColorSignIn : disabledColor);
        enableButton(signUp, checked, checked ? enabledColorSignUp : disabledColor);
    }

    void enableButton(Button button, boolean checked, int color){
        button.setEnabled(checked);
        button.setBackgroundColor(color);
    }

    @OnClick(R.id.accept_terms_text)
    void onClickedTermsText(){
        startActivity(new Intent(this, TermsAndConditionActivity.class));
    }

}


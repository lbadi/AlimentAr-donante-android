package proyectoalimentar.alimentardonanteapp.ui.signUp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;

import proyectoalimentar.alimentardonanteapp.ui.login.LoginActivity;

public class MainDataSignUpFragment extends Fragment {

    @BindView(R.id.email)
    EditText emailField;
    @BindView(R.id.password)
    EditText passwordField;
    @BindView(R.id.sign_up_button)
    Button signUpButton;
    @BindView(R.id.already_have_account_button)
    Button alreadyHaveAccountButton;

    public OnRegisterAttemptListener onRegisterAttepmtListener;

    @Inject LayoutInflater layoutInflater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AlimentarApp.inject(this);
        View view = layoutInflater.inflate(R.layout.sign_up_main_data_fragment, container, false);
        ButterKnife.bind(this,view);

        signUpButton.setOnClickListener( v -> attemptRegister());
        alreadyHaveAccountButton.setOnClickListener(v -> goToLogin());

        return view;
    }

    public void setOnRegisterAttemptListener(OnRegisterAttemptListener onRegisterAttepmtListener){
        this.onRegisterAttepmtListener = onRegisterAttepmtListener;
    }

    private void attemptRegister(){

        View focusView = null;
        boolean cancel = false;
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();


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

        if(cancel){
            focusView.requestFocus();
        }else{

            if(onRegisterAttepmtListener == null){
                throw new IllegalArgumentException("OnRegisterAttemptListener not setted");
            }
            onRegisterAttepmtListener.onRegisterAttempt(email,password);


        }

    }

    private void goToLogin(){
        startActivity(new Intent(getActivity(), LoginActivity.class));
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() >= 8;
    }

    public interface OnRegisterAttemptListener{
        public void onRegisterAttempt(String email, String password);
    }

}

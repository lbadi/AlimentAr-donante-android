package proyectoalimentar.alimentardonanteapp.ui.terms_and_condition;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;
import proyectoalimentar.alimentardonanteapp.ui.signUp.MainDataSignUpFragment;

public class TermsAndConditionActivity extends AppCompatActivity{

    FragmentManager fragmentManager = getSupportFragmentManager();
    TermsAndConditionFragment termsAndConditionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.terms_activity);
        ButterKnife.bind(this);
        initTermsFragment();
    }

    private void initTermsFragment(){
        //Create and initialize terms fragment.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        termsAndConditionFragment = new TermsAndConditionFragment();
        fragmentTransaction.add(R.id.fragment_container,termsAndConditionFragment);
        fragmentTransaction.commit();
    }
}

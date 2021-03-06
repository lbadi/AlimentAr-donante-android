package proyectoalimentar.alimentardonanteapp.di.component;



import javax.inject.Singleton;

import dagger.Component;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.di.module.AppModule;
import proyectoalimentar.alimentardonanteapp.di.module.NetworkModule;
import proyectoalimentar.alimentardonanteapp.services.RegistrationIntentService;
import proyectoalimentar.alimentardonanteapp.ui.donations.ActivatedQuestionView;
import proyectoalimentar.alimentardonanteapp.ui.donations.CancelDonationView;
import proyectoalimentar.alimentardonanteapp.ui.donations.DonationFragment;
import proyectoalimentar.alimentardonanteapp.ui.donations.ItemFragment;
import proyectoalimentar.alimentardonanteapp.ui.donations.NewDonationFragment;
import proyectoalimentar.alimentardonanteapp.ui.donations.QualifyVolunteerView;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;
import proyectoalimentar.alimentardonanteapp.ui.login.LoginActivity;
import proyectoalimentar.alimentardonanteapp.ui.profile.ProfileFragment;
import proyectoalimentar.alimentardonanteapp.ui.signOut.SignOutFragment;
import proyectoalimentar.alimentardonanteapp.ui.signUp.AditionalDataSignUpFragment;
import proyectoalimentar.alimentardonanteapp.ui.signUp.MainDataSignUpFragment;
import proyectoalimentar.alimentardonanteapp.ui.signUp.SignUpActivity;
import proyectoalimentar.alimentardonanteapp.ui.terms_and_condition.TermsAndConditionFragment;
import proyectoalimentar.alimentardonanteapp.ui.view.DonationView;
import proyectoalimentar.alimentardonanteapp.ui.view.ItemView;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    void inject(LoginActivity activity);

    void inject(SignUpActivity activity);

    void inject(NewDonationFragment activity);

    void inject(MainDataSignUpFragment fragment);

    void inject(AditionalDataSignUpFragment fragment);

    void inject(TermsAndConditionFragment fragment);

    void inject(SignOutFragment fragment);

    void inject(DonationFragment fragment);

    void inject(CancelDonationView view);

    void inject(DonationView view);

    void inject(ItemView view);

    void inject (ItemFragment itemFragment);

    void inject(RegistrationIntentService service);

    void inject (DrawerActivity drawerActivity);

    void inject (ActivatedQuestionView activatedQuestionView);

    void inject (ProfileFragment profileFragment);

    void inject (QualifyVolunteerView qualifyVolunteerView);

}


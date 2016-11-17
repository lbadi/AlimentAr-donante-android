package proyectoalimentar.alimentardonanteapp;

import android.app.Application;
import android.content.Context;

import net.danlew.android.joda.JodaTimeAndroid;

import proyectoalimentar.alimentardonanteapp.di.component.DaggerAppComponent;
import proyectoalimentar.alimentardonanteapp.di.module.AppModule;
import proyectoalimentar.alimentardonanteapp.di.module.NetworkModule;
import proyectoalimentar.alimentardonanteapp.services.RegistrationIntentService;
import proyectoalimentar.alimentardonanteapp.ui.donations.DonationFragment;
import proyectoalimentar.alimentardonanteapp.ui.donations.NewDonationFragment;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;
import proyectoalimentar.alimentardonanteapp.ui.login.LoginActivity;
import proyectoalimentar.alimentardonanteapp.ui.signOut.SignOutFragment;
import proyectoalimentar.alimentardonanteapp.ui.signUp.AditionalDataSignUpFragment;
import proyectoalimentar.alimentardonanteapp.ui.signUp.MainDataSignUpFragment;
import proyectoalimentar.alimentardonanteapp.ui.signUp.SignUpActivity;
import proyectoalimentar.alimentardonanteapp.ui.terms_and_condition.TermsAndConditionFragment;
import proyectoalimentar.alimentardonanteapp.ui.view.DonationView;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class AlimentarApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        JodaTimeAndroid.init(this);
        setupCalligraphy();
    }

    public static Context getContext() {
        return context;
    }



    public static void inject(LoginActivity target) {
        DaggerAppComponent.builder()
                .appModule(new AppModule(target))
                .build()
                .inject(target);
    }

    public static void inject(MainDataSignUpFragment fragment){
        DaggerAppComponent.builder()
                .appModule(new AppModule(fragment.getActivity()))
                .build()
                .inject(fragment);
    }

    public static void inject(AditionalDataSignUpFragment fragment){
        DaggerAppComponent.builder()
                .appModule(new AppModule(fragment.getActivity()))
                .build()
                .inject(fragment);
    }

    public static void inject(SignUpActivity target) {
        DaggerAppComponent.builder()
                .appModule(new AppModule(target))
                .networkModule(new NetworkModule())
                .build()
                .inject(target);
    }

    public static void inject(NewDonationFragment target) {
        DaggerAppComponent.builder()
                .appModule(new AppModule(target.getActivity()))
                .build()
                .inject(target);
    }

    public static void inject(TermsAndConditionFragment fragment){
        DaggerAppComponent.builder()
                .appModule(new AppModule(fragment.getActivity()))
                .build()
                .inject(fragment);
    }

    public static void inject(SignOutFragment fragment){
        DaggerAppComponent.builder()
                .appModule(new AppModule(fragment.getActivity()))
                .build()
                .inject(fragment);
    }

    public static void inject(DonationFragment fragment){
        DaggerAppComponent.builder()
                .appModule(new AppModule(fragment.getActivity()))
                .build()
                .inject(fragment);
    }

    public static void inject(DonationView view){
        DaggerAppComponent.builder()
                .appModule(new AppModule(view.getContext()))
                .build()
                .inject(view);
    }

    public static void inject(RegistrationIntentService service){
        DaggerAppComponent.builder()
                .appModule(new AppModule(service))
                .build()
                .inject(service);
    }

    public static void inject(DrawerActivity activity){
        DaggerAppComponent.builder()
                .appModule(new AppModule(activity))
                .build()
                .inject(activity);
    }

    private void setupCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}

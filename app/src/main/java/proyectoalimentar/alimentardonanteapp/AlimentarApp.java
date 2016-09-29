package proyectoalimentar.alimentardonanteapp;

import android.app.Application;
import android.content.Context;

import proyectoalimentar.alimentardonanteapp.di.component.DaggerAppComponent;
import proyectoalimentar.alimentardonanteapp.di.module.AppModule;
import proyectoalimentar.alimentardonanteapp.login.LoginActivity;
import proyectoalimentar.alimentardonanteapp.login.SignUpActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class AlimentarApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
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
    public static void inject(SignUpActivity target) {
        DaggerAppComponent.builder()
                .appModule(new AppModule(target))
                .build()
                .inject(target);
    }

    private void setupCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Medium.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

}

package proyectoalimentar.alimentardonanteapp.di.component;



import javax.inject.Singleton;

import dagger.Component;
import proyectoalimentar.alimentardonanteapp.di.module.AppModule;
import proyectoalimentar.alimentardonanteapp.login.LoginActivity;
import proyectoalimentar.alimentardonanteapp.login.SignUpActivity;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(LoginActivity activity);

    void inject(SignUpActivity activity);


}


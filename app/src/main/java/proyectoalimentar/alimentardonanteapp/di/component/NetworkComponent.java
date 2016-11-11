package proyectoalimentar.alimentardonanteapp.di.component;

import javax.inject.Singleton;

import dagger.Component;
import proyectoalimentar.alimentardonanteapp.di.module.NetworkModule;
import proyectoalimentar.alimentardonanteapp.ui.login.LoginActivity;
import proyectoalimentar.alimentardonanteapp.ui.signUp.SignUpActivity;

@Singleton
@Component(modules = {NetworkModule.class})
public interface NetworkComponent {

    void inject(LoginActivity activity);

    void inject(SignUpActivity activity);


}

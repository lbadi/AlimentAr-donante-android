package proyectoalimentar.alimentardonanteapp.di.module;

import dagger.Module;
import dagger.Provides;
import proyectoalimentar.alimentardonanteapp.network.DonationService;
import proyectoalimentar.alimentardonanteapp.network.LoginService;
import proyectoalimentar.alimentardonanteapp.network.NotificationService;
import proyectoalimentar.alimentardonanteapp.network.RetrofitServices;

@Module
public class NetworkModule {

    @Provides
    LoginService provideLoginService(){
        return RetrofitServices.getService(LoginService.class);
    }

    @Provides
    DonationService provideDonationService(){
        return RetrofitServices.getService(DonationService.class);
    }

    @Provides
    NotificationService provideNotificationService(){
        return RetrofitServices.getService(NotificationService.class);
    }
}

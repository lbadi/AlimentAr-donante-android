package proyectoalimentar.alimentardonanteapp.repository;

import android.content.Intent;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import javax.inject.Inject;
import javax.inject.Singleton;

import proyectoalimentar.alimentardonanteapp.model.AuthenticatedUser;
import proyectoalimentar.alimentardonanteapp.network.LoginService;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;
import proyectoalimentar.alimentardonanteapp.utils.UserStorage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class UserRepository {

    @Inject
    LoginService loginService;
    @Inject
    UserStorage userStorage;

    @Inject
    public UserRepository(){

    }

    public void login(String email, String password, RepoCallBack<AuthenticatedUser> callBack){
        loginService.login(email,password).enqueue(new Callback<AuthenticatedUser>() {
            @Override
            public void onResponse(Call<AuthenticatedUser> call, Response<AuthenticatedUser> response) {
                if (response.isSuccessful()) {
                    AuthenticatedUser user = response.body();
                    user.setEmail(email); //Set email before saving in shared preferences
                    userStorage.login(user);
                    callBack.onSuccess(user);
                } else {
                    callBack.onError(null);
                }
            }

            @Override
            public void onFailure(Call<AuthenticatedUser> call, Throwable t) {
                callBack.onError(t.getMessage());
            }
        });
    }

    public void signUp(String email,String password,String name,String direction,
                       RepoCallBack<AuthenticatedUser> callBack){
        loginService.signUp(email,password,name,direction).enqueue(new Callback<AuthenticatedUser>() {
            @Override
            public void onResponse(Call<AuthenticatedUser> call, Response<AuthenticatedUser> response) {
                if (response.isSuccessful()) {
                    AuthenticatedUser user = response.body();
                    user.setEmail(email); //Set email before saving in shared preferences
                    userStorage.login(user);
                    callBack.onSuccess(user);
                } else {
                    callBack.onError(null);
                }
            }

            @Override
            public void onFailure(Call<AuthenticatedUser> call, Throwable t) {
                callBack.onError(t.getMessage());
            }
        });
    }
}

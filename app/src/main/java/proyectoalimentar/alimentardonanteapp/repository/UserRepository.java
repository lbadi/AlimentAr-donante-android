package proyectoalimentar.alimentardonanteapp.repository;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import proyectoalimentar.alimentardonanteapp.Configuration;
import proyectoalimentar.alimentardonanteapp.model.AuthenticatedUser;
import proyectoalimentar.alimentardonanteapp.model.Avatar;
import proyectoalimentar.alimentardonanteapp.model.Donator;
import proyectoalimentar.alimentardonanteapp.network.UserService;
import proyectoalimentar.alimentardonanteapp.network.NotificationService;
import proyectoalimentar.alimentardonanteapp.utils.StorageUtils;
import proyectoalimentar.alimentardonanteapp.utils.UserStorage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class UserRepository {

    @Inject
    UserService userService;
    @Inject
    UserStorage userStorage;
    @Inject
    NotificationService notificationService;

    @Inject
    public UserRepository(){

    }

    public void login(String email, String password, RepoCallBack<AuthenticatedUser> callBack){
        userService.login(email,password).enqueue(new Callback<AuthenticatedUser>() {
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
        userService.signUp(email,password,name,direction).enqueue(new Callback<AuthenticatedUser>() {
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

    public void signOut(RepoCallBack<Void> callBack){
        final String token = StorageUtils.getStringFromSharedPreferences(Configuration.TOKEN, null);
        if(token != null && !token.isEmpty()){
            notificationService.deleteToken(token).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if(response.isSuccessful()){
                        Log.i("Delete token", "Delete woken was successfully deleted");
                        userStorage.logout();
                        removeToken();
                        callBack.onSuccess(null);
                    }else{
                        callBack.onError(null);
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    //Todo Try to delete token again, exponential backoff
                    callBack.onError(t.getMessage());
                }
            });
        }else{
            userStorage.logout();
            removeToken();
            callBack.onSuccess(null);
        }

    }

    /**
     * Get donator logged information using cached data
     * @param repoCallBack
     */
    public void getMyInformation(RepoCallBack<Donator> repoCallBack){
        getMyInformation(repoCallBack,true);
    }

    public void getMyInformation(RepoCallBack<Donator> repoCallBack, boolean useCache){
        Donator donator = userStorage.getCachedDonator();
        if( useCache && donator != null){ //Try to use the cached data.
            repoCallBack.onSuccess(donator);
            return;
        }
        userService.getMyInformation().enqueue(new Callback<Donator>() {
            @Override
            public void onResponse(Call<Donator> call, Response<Donator> response) {
                if(response.isSuccessful()){
                    StorageUtils.storeInSharedPreferences(Configuration.USER_INFORMATION,response.body());
                    repoCallBack.onSuccess(response.body());
                }else{
                    repoCallBack.onError(null);
                }
            }

            @Override
            public void onFailure(Call<Donator> call, Throwable t) {
                repoCallBack.onError(t.getMessage());
            }
        });
    }

    public void changeProfile(String name, RepoCallBack<Void> repoCallBack){
        final Donator donator = userStorage.getCachedDonator(); //TODO donator can be null in a rare case
        userService.update(donator.getId(), name).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    donator.setName(name);
                    userStorage.setCachedDonator(donator);
                    repoCallBack.onSuccess(null);
                }else{
                    repoCallBack.onError(null);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                repoCallBack.onError(t.getMessage());
            }
        });
    }

    private void removeToken(){
        StorageUtils.clearKey(Configuration.SENT_TOKEN_TO_SERVER); //Clear token
    }

    private boolean isTokenPresent(){
        return StorageUtils.getBooleanFromSharedPreferences(Configuration.SENT_TOKEN_TO_SERVER, false);
    }
}

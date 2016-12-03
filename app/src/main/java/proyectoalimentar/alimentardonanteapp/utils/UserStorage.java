package proyectoalimentar.alimentardonanteapp.utils;



import javax.inject.Inject;
import javax.inject.Singleton;

import proyectoalimentar.alimentardonanteapp.Configuration;
import proyectoalimentar.alimentardonanteapp.model.AuthenticatedUser;
import proyectoalimentar.alimentardonanteapp.model.Donator;
import proyectoalimentar.alimentardonanteapp.model.User;

@Singleton
public class UserStorage {

    private static final String LOGGED_USER = "LOGGED_USER";

    private AuthenticatedUser loggedUser;

    @Inject
    UserStorage() {
    }

    public AuthenticatedUser getLoggedUser() {
        if (loggedUser == null) {
            loggedUser = StorageUtils.getObjectFromSharedPreferences(LOGGED_USER, AuthenticatedUser.class);
        }
        return loggedUser;
    }

    public void login(AuthenticatedUser authenticatedUser) {
        loggedUser = authenticatedUser;
        StorageUtils.storeInSharedPreferences(
                Configuration.ACCESS_TOKEN, authenticatedUser.getAccessToken());
        StorageUtils.storeInSharedPreferences(LOGGED_USER, authenticatedUser);
    }

    public void logout(){
        loggedUser = null;
        cleanCachedDonator();
        StorageUtils.clearKey(Configuration.ACCESS_TOKEN);
        StorageUtils.clearKey(LOGGED_USER);
    }

    public boolean isUserLogged() {
        return getLoggedUser() != null;
    }

    /**
     * Get the donator information from shared preferences
     * @return The cached donator(logged in) information. @null if doesn't have anything cached.
     */
    public Donator getCachedDonator(){
        return StorageUtils.getObjectFromSharedPreferences(Configuration.USER_INFORMATION,Donator.class);
    }

    public void setCachedDonator(Donator donator){
        StorageUtils.storeInSharedPreferences(Configuration.USER_INFORMATION, donator);
    }

    private void cleanCachedDonator(){
        StorageUtils.clearKey(Configuration.USER_INFORMATION);
    }

}

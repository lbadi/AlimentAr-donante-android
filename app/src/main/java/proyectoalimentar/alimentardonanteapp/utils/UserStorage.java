package proyectoalimentar.alimentardonanteapp.utils;



import javax.inject.Inject;
import javax.inject.Singleton;

import proyectoalimentar.alimentardonanteapp.Configuration;
import proyectoalimentar.alimentardonanteapp.model.AuthenticatedUser;
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
        StorageUtils.clearKey(Configuration.ACCESS_TOKEN);
        StorageUtils.clearKey(LOGGED_USER);
    }

    public boolean isUserLogged() {
        return getLoggedUser() != null;
    }

}

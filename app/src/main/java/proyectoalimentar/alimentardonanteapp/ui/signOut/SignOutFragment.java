package proyectoalimentar.alimentardonanteapp.ui.signOut;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;
import proyectoalimentar.alimentardonanteapp.ui.login.LoginActivity;
import proyectoalimentar.alimentardonanteapp.utils.UserStorage;

public class SignOutFragment extends Fragment{


    @Inject
    UserStorage  userStorage;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        AlimentarApp.inject(this);

        userStorage.logout();
        startActivity(new Intent(this.getActivity(), LoginActivity.class));

    }
}

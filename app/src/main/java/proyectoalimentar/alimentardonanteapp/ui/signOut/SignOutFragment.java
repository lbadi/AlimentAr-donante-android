package proyectoalimentar.alimentardonanteapp.ui.signOut;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.Configuration;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.repository.RepoCallBack;
import proyectoalimentar.alimentardonanteapp.repository.UserRepository;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;
import proyectoalimentar.alimentardonanteapp.ui.login.LoginActivity;
import proyectoalimentar.alimentardonanteapp.utils.StorageUtils;
import proyectoalimentar.alimentardonanteapp.utils.UserStorage;

public class SignOutFragment extends Fragment{


    private static final long exponentialBackOffTimeLimit = 1000 * 5; //5 sec limit

    @Inject
    UserStorage  userStorage;
    @Inject
    UserRepository userRepository;

    long exponentialBackOffTime = 1;

    @Override
    public void onCreate(Bundle bundle){
        super.onCreate(bundle);
        AlimentarApp.inject(this);

        userRepository.signOut(createSimpleRepoCallBack());
    }

    private RepoCallBack<Void> createSimpleRepoCallBack(){
        return new RepoCallBack<Void>() {
            @Override
            public void onSuccess(Void value) {
                startActivity(new Intent(getActivity(), LoginActivity.class));
            }

            @Override
            public void onError(String error) {
                if(exponentialBackOffTime > exponentialBackOffTimeLimit){
                    if (getActivity() == null || getActivity().isFinishing()) {
                        return;
                    }
                    Toast.makeText(getContext(),R.string.error_signout, Toast.LENGTH_SHORT);
                }else {
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        userRepository.signOut(createSimpleRepoCallBack());
                    }, exponentialBackOffTime);
                    exponentialBackOffTime = exponentialBackOffTime * 2;
                }
            }
        };
    }

}

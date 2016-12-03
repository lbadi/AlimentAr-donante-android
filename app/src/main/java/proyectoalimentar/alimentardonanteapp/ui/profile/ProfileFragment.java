package proyectoalimentar.alimentardonanteapp.ui.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.model.Donator;
import proyectoalimentar.alimentardonanteapp.repository.RepoCallBack;
import proyectoalimentar.alimentardonanteapp.repository.UserRepository;
import proyectoalimentar.alimentardonanteapp.utils.UserStorage;


public class ProfileFragment extends Fragment{

    @Inject
    LayoutInflater layoutInflater;
    @Inject
    UserRepository userRepository;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_name)
    EditText name;
    @BindView(R.id.edit_name_ic)
    ImageView editIc;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    Donator donator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AlimentarApp.inject(this);
        View view = layoutInflater.inflate(R.layout.profile_fragment,container,false);
        ButterKnife.bind(this,view);
        setupDrawer();
        fillInformation();
        return view;
    }

    private void fillInformation(){
        progressBar.setVisibility(View.VISIBLE);
        userRepository.getMyInformation(new RepoCallBack<Donator>() {
            @Override
            public void onSuccess(Donator donator) {
                name.setText(donator.getName());
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(),R.string.error_fetching_donator,Toast.LENGTH_SHORT);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void setupDrawer() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> ( getActivity()).onBackPressed());
    }

    @OnClick(R.id.edit_name_ic)
    public void edit(){
        name.setFocusableInTouchMode(true);
        name.requestFocus();
    }

    @OnClick(R.id.change_profile)
    public void changeProfile(){
        if(!validateInput()){
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        userRepository.changeProfile(name.getText().toString(), new RepoCallBack<Void>() {
            @Override
            public void onSuccess(Void value) {
                progressBar.setVisibility(View.GONE);
                getActivity().onBackPressed();
            }

            @Override
            public void onError(String error) {
                Toast.makeText(getContext(),R.string.error_updating_donator,Toast.LENGTH_SHORT);
            }
        });
    }

    private boolean validateInput(){

        name.setError(null);
        boolean valid = true;

        if(name.getText().toString().isEmpty()){
            valid = false;
            name.setError(getResources().getString(R.string.empty_field));
        }
        return valid;
    }
}

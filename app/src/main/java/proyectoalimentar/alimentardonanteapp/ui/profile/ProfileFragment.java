package proyectoalimentar.alimentardonanteapp.ui.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;


public class ProfileFragment extends Fragment{

    @Inject
    LayoutInflater layoutInflater;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_name)
    EditText name;
    @BindView(R.id.edit_name_ic)
    ImageView editIc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AlimentarApp.inject(this);
        View view = layoutInflater.inflate(R.layout.profile_fragment,container,false);
        ButterKnife.bind(this,view);
        setupDrawer();

        return view;
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
}

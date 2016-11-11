package proyectoalimentar.alimentardonanteapp.ui.terms_and_condition;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.R;


import javax.inject.Inject;

import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;


public class TermsAndConditionFragment extends Fragment{


    @Inject
    LayoutInflater layoutInflater;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AlimentarApp.inject(this);
        View view = layoutInflater.inflate(R.layout.terms_fragment,container,false);
        ButterKnife.bind(this,view);
        setupDrawer();

        return view;
    }

    private void setupDrawer() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> ( getActivity()).onBackPressed());
    }
}

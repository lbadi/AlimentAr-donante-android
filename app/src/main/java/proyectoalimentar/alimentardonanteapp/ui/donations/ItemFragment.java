package proyectoalimentar.alimentardonanteapp.ui.donations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;

/**
 * A fragment representing a list of items (the content of a donation)
 * */

public class ItemFragment extends Fragment{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AlimentarApp.inject(this);
        View view = inflater.inflate(R.layout.item_fragmet, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init(){

    }

}

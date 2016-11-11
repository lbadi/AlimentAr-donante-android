package proyectoalimentar.alimentardonanteapp.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * A fragment witouth layout that manipulates the option menu.
 */
public class MenuBehaviourFragment extends Fragment {



    public MenuBehaviourFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of the fragment.
     * @return A new instance of fragment MenuBehaviourFragment.
     */
    public static MenuBehaviourFragment newInstance() {
        MenuBehaviourFragment fragment = new MenuBehaviourFragment();
        fragment.setRetainInstance(true);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}

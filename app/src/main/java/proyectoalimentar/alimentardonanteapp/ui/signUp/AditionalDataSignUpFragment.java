package proyectoalimentar.alimentardonanteapp.ui.signUp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;



public class AditionalDataSignUpFragment extends Fragment{

    private static final int PLACE_PICKER_REQUEST = 1;
    public static final LatLng NORTHEAST_BORDER = new LatLng(-34.542276,-58.361092);
    public static final LatLng SOUTHWEST_BORDER = new LatLng(-34.666050,-58.520393);

    Place pickedPlace;
    OnRegisterFinishAttemptListener listener;


    @BindView(R.id.nameField)
    EditText nameField;
    @BindView(R.id.pick_address_button)
    Button pickPlaceButton;
    @BindView(R.id.pick_addres_text)
    TextView pickAddressText;
    @BindView(R.id.sign_up_button)
    Button signUpButton;

    @Inject
    LayoutInflater layoutInflater;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        AlimentarApp.inject(this);
        View view = layoutInflater.inflate(R.layout.sign_up_aditional_data_frament, container, false);
        ButterKnife.bind(this,view);

        signUpButton.setOnClickListener( v -> attemptRegister());
        pickPlaceButton.setOnClickListener( v -> pickPlace());
        return view;
    }

    public void attemptRegister(){
        View focusView = null;
        boolean cancel = false;
        String name = nameField.getText().toString();
        if(!isNameValid(name)){
            focusView = nameField;
            nameField.setError(getResources().getString(R.string.error_invalid_name));
            cancel = true;
        }
        if(pickedPlace == null){
            focusView = pickPlaceButton;
            pickAddressText.setError(getResources().getString(R.string.error_pick_address));
            cancel = true;
        }

        if(cancel){
            focusView.requestFocus();
            return;
        }

        listener.onRegisterFinish(name,pickedPlace);

    }

    private void pickPlace(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        builder.setLatLngBounds(new LatLngBounds(SOUTHWEST_BORDER, NORTHEAST_BORDER));
        try {
            startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                pickedPlace = PlacePicker.getPlace(getActivity(), data);
                pickAddressText.setText(pickedPlace.getAddress());
            }
        }
    }

    public void setOnRegisterFinishAttemptListener(OnRegisterFinishAttemptListener onRegisterFinishAttemptListener) {
        this.listener = onRegisterFinishAttemptListener;
    }

    public interface OnRegisterFinishAttemptListener{
        void onRegisterFinish(String name, Place direction);
    }

    private boolean isNameValid(String name){
        return !name.isEmpty() && name.length() < 255;
    }

}

package proyectoalimentar.alimentardonanteapp.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
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

import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.internal.Utils;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.model.Donator;
import proyectoalimentar.alimentardonanteapp.repository.RepoCallBack;
import proyectoalimentar.alimentardonanteapp.repository.UserRepository;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;
import proyectoalimentar.alimentardonanteapp.utils.UserStorage;


public class ProfileFragment extends Fragment{

    private static final int PICK_IMAGE = 1;

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
    @BindView(R.id.profile_image)
    ImageView profileImage;

    Donator donator;
    boolean imageChange = false;

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
                if(!imageChange){
                    progressBar.setVisibility(View.GONE);
                    DrawerActivity drawerActivity = (DrawerActivity) getActivity();
                    drawerActivity.fetchDonatorInformation();
                    drawerActivity.onBackPressed();
                }

            }

            @Override
            public void onError(String error) {
                if(!imageChange){
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),R.string.error_updating_donator,Toast.LENGTH_SHORT);
                }
            }
        });
        if(imageChange){
            userRepository.uploadPhoto(((BitmapDrawable) profileImage.getDrawable()).getBitmap(), new RepoCallBack<Boolean>() {
                @Override
                public void onSuccess(Boolean value) {
                    progressBar.setVisibility(View.GONE);
                    successfullyUpload();
                }

                @Override
                public void onError(String error) {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(),R.string.error_updating_donator,Toast.LENGTH_SHORT);
                }
            });
        }
    }

    private void successfullyUpload(){
        DrawerActivity drawerActivity = (DrawerActivity) getActivity();
        drawerActivity.fetchDonatorInformation();
        drawerActivity.onBackPressed();
    }

    @OnClick(R.id.profile_image)
    public void selectImage(){
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE) {
            if(resultCode == Activity.RESULT_OK){
                if(data != null){
                    try {
                        InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        profileImage.setImageBitmap(bitmap);
                        imageChange = true;
                    } catch (FileNotFoundException e) {
                        //
                    }
                }
            }
        }
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

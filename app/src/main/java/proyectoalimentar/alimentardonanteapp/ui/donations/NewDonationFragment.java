package proyectoalimentar.alimentardonanteapp.ui.donations;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.model.Donation;
import proyectoalimentar.alimentardonanteapp.network.DonationService;
import proyectoalimentar.alimentardonanteapp.network.LoginService;
import proyectoalimentar.alimentardonanteapp.network.RetrofitServices;
import proyectoalimentar.alimentardonanteapp.repository.DonationRepository;
import proyectoalimentar.alimentardonanteapp.repository.RepoCallBack;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.lang.reflect.Method;

import javax.inject.Inject;

public class NewDonationFragment extends Fragment{

    @BindView(R.id.description_text)
    EditText descriptionText;
//    @BindView(R.id.pickup_date_from)
//    TextView pickupDateFrom;
    @BindView(R.id.pickup_time_from)
    TextView pickupTimeFrom;
//    @BindView(R.id.pickup_date_to)
//    TextView pickupDateTo;
    @BindView(R.id.pickup_time_to)
    TextView pickupTimeTo;
    @BindView(R.id.confirm_donation)
    ImageView createDonationButton;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.from_layout)
    LinearLayout fromLayout;
    @BindView(R.id.to_layout)
    LinearLayout toLayout;
    @BindView(R.id.all_day_switch)
    Switch allDaySwitch;

    @Inject
    LayoutInflater layoutInflater;
    @Inject
    DonationRepository donationRepository;

    OnDonationCreatedListener onDonationCreatedListener;

    DateTimeFormatter formatDate = DateTimeFormat.forPattern("dd-MM-YYYY");
    DateTimeFormatter formatTime = DateTimeFormat.forPattern("HH-mm");

    DateTime dateTimeFrom;
    DateTime dateTimeTo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        AlimentarApp.inject(this);
        View view = layoutInflater.inflate(R.layout.new_donation_fragment,container,false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    public void init(){
        createDonationButton.setOnClickListener(v -> attemptCreateDonation());
//        pickupDateFrom.setOnClickListener( v -> showDatePickerDialog(pickupDateFrom));
//        setUpCustomListenersDate(pickupDateFrom);
//        setUpCustomListenersDate(pickupDateTo);
        setUpCustomListenersTime(pickupTimeFrom);
        setUpCustomListenersTime(pickupTimeTo);
        setSwitchChangeListener();
        setupDrawer();
    }

    private void setSwitchChangeListener(){
        allDaySwitch.setOnCheckedChangeListener((compoundButton, value) -> {
            if(value){
                setEnablePickUpTimes(false);
            }else{
                setEnablePickUpTimes(true);
            }
        });
    }

    private void setEnablePickUpTimes(boolean value){
        if(value) {
            fromLayout.setVisibility(LinearLayout.VISIBLE);
            toLayout.setVisibility(LinearLayout.VISIBLE);
        }else{
            //Set time to start/end of the actual day
            pickupTimeFrom.setText(formatTime.print(new DateTime().withTimeAtStartOfDay()));
            pickupTimeTo.setText(formatTime.print(new DateTime().plusDays(1).withTimeAtStartOfDay().minusMinutes(1)));
            fromLayout.setVisibility(LinearLayout.GONE);
            toLayout.setVisibility(LinearLayout.GONE);
        }
    }

    private void setUpCustomListenersDate(TextView view){
        view.setOnFocusChangeListener((v, getFocus) -> {
            if (getFocus){
                showDatePickerDialog(view);
            };
        });
        view.setOnClickListener(v -> showDatePickerDialog(view));
    }

    private void setUpCustomListenersTime(TextView view){
        view.setOnFocusChangeListener((v, getFocus) -> {
            if (getFocus){
                showTimePickerDialog(view);
            };
        });
        view.setOnClickListener(v -> showTimePickerDialog(view));
    }

    public void attemptCreateDonation(){

        if(!validateInput()){
            return;
        }
        //Form dateTime from textfields
//        dateTimeFrom = formatDate.parseDateTime(pickupDateFrom.getText().toString())
//                .withTime(formatTime.parseLocalTime(pickupTimeFrom.getText().toString()));
        dateTimeFrom = new DateTime()
                .withTime(formatTime.parseLocalTime(pickupTimeFrom.getText().toString()));
//        dateTimeTo = formatDate.parseDateTime(pickupDateTo.getText().toString())
//                .withTime(formatTime.parseLocalTime(pickupTimeTo.getText().toString()));
        dateTimeTo = new DateTime()
                .withTime(formatTime.parseLocalTime(pickupTimeTo.getText().toString()));
        String description = descriptionText.getText().toString();

        donationRepository.createDonation(description, dateTimeFrom.toString(),
                dateTimeTo.toString(), new RepoCallBack<Boolean>() {
            @Override
            public void onSuccess(Boolean created) {
                if(created){
                    if(onDonationCreatedListener != null){
                        onDonationCreatedListener.onDonationCreated();
                    }
                }

            }

            @Override
            public void onError(String error) {
                showCreateError();
            }
        });
    }

    private void showCreateError(){
        Toast.makeText(getActivity(),R.string.error_create_donation,Toast.LENGTH_SHORT);
    }

    private boolean validateInput(){

        //Clean errors
//        pickupDateFrom.setError(null);
//        pickupDateTo.setError(null);
        pickupTimeFrom.setError(null);
        pickupTimeTo.setError(null);

        boolean valid = true;

        if(pickupTimeFrom.getText().toString().isEmpty()){
            valid = false;
            pickupTimeFrom.setError(getResources().getString(R.string.empty_field));
        }
//        if(pickupDateFrom.getText().toString().isEmpty()){
//            valid = false;
//            pickupDateFrom.setError(getResources().getString(R.string.empty_field));
//        }
        if(pickupTimeTo.getText().toString().isEmpty()){
            valid = false;
            pickupTimeTo.setError(getResources().getString(R.string.empty_field));
        }
//        if(pickupDateTo.getText().toString().isEmpty()){
//            valid = false;
//            pickupDateTo.setError(getResources().getString(R.string.empty_field));
//        }
        return valid;
    }

    public boolean showDatePickerDialog(final TextView view){
        DialogFragment datePickerFragment = DatePickerFragment.newInstance((year, month, day) -> {
            DateTime dateTime = new DateTime()
                    .withYear(year)
                    .withMonthOfYear(month+1) //DatePicker returns month beetween [0-11] and jodaTime use it from [1-12]
                    .withDayOfMonth(day);
            view.setText(formatDate.print(dateTime));
        });
        datePickerFragment.show(getActivity().getSupportFragmentManager(), view.toString() + "Date");
        return true;
    }

    public void showTimePickerDialog(final TextView view) {
        DialogFragment timePickerFragment = TimePickerFragment.newInstance((hour, minute) ->{
            DateTime dateTime = new DateTime()
                    .withHourOfDay(hour)
                    .withMinuteOfHour(minute);
            view.setText(formatTime.print(dateTime));
        });
        timePickerFragment.show(getActivity().getSupportFragmentManager(), view.toString() + "Time");
    }

    private void setupDrawer() {
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v ->  getActivity().onBackPressed());
    }

    public void setOnDonationCreated(OnDonationCreatedListener onDonationCreatedListener) {
        this.onDonationCreatedListener = onDonationCreatedListener;
    }

    public interface OnDonationCreatedListener{
        void onDonationCreated();
    }

}

package proyectoalimentar.alimentardonanteapp.ui.donations;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.model.Item;
import proyectoalimentar.alimentardonanteapp.repository.DonationRepository;
import proyectoalimentar.alimentardonanteapp.repository.RepoCallBack;
import proyectoalimentar.alimentardonanteapp.repository.UtilRepository;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Text;

import java.util.Locale;

import javax.inject.Inject;

public class NewDonationFragment extends Fragment{

    //How many hours it will be added to pickup to datetime default.
    private static final int PLUS_HOURS_DEFAULT = 2;

    @BindView(R.id.description_text)
    EditText descriptionText;
    /** Time and date selectors*/
    @BindView(R.id.pickup_date_from)
    TextView pickupDateFrom;
    @BindView(R.id.pickup_time_from)
    TextView pickupTimeFrom;
    @BindView(R.id.pickup_date_to)
    TextView pickupDateTo;
    @BindView(R.id.pickup_time_to)
    TextView pickupTimeTo;
    /** ---------------- */
    @BindView(R.id.datetime_layout)
    LinearLayout dateTimeLayout;
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
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.item_list)
    RecyclerView itemList;
    @BindView(R.id.new_item_button)
    View newItemButton;


    @Inject
    LayoutInflater layoutInflater;
    @Inject
    DonationRepository donationRepository;
    @Inject
    UtilRepository utilRepository;

    OnDonationCreatedListener onDonationCreatedListener;

    DateTimeFormatter formatDate = DateTimeFormat.forPattern("EEE, MMM dd,YYYY").withLocale(Locale.getDefault());
    DateTimeFormatter formatTime = DateTimeFormat.forPattern("hh:mm a").withLocale(Locale.getDefault());

    DateTime dateTimeFrom;
    DateTime dateTimeTo;

    CharSequence lastTimeSelectedFrom;
    CharSequence lastTimeSelectedTo;

    ItemAdapter itemAdapter;

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
        setSwitchChangeListener();
        setDateAndTimeComponents();
        setupDrawer();
        setupItems();
        newItemButton.setOnClickListener( v -> addItem());
    }

    private void setupItems(){
        itemAdapter = new ItemAdapter(utilRepository);
        itemList.setAdapter(itemAdapter);
        itemList.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void setDateAndTimeComponents(){
        //Set defaults values.
        DateTime initialFromDateTime = new DateTime();
        pickupDateFrom.setText(formatDate.print(initialFromDateTime));
        pickupTimeFrom.setText(formatTime.print(initialFromDateTime));
        DateTime initialToDateTime = new DateTime().plusHours(PLUS_HOURS_DEFAULT);
        pickupDateTo.setText(formatDate.print(initialToDateTime));
        pickupTimeTo.setText(formatTime.print(initialToDateTime));

        //Init listeners
        setUpCustomListenersDate(pickupDateFrom);
        setUpCustomListenersDate(pickupDateTo);
        setUpCustomListenersTime(pickupTimeFrom);
        setUpCustomListenersTime(pickupTimeTo);
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
            pickupTimeFrom.setText(lastTimeSelectedFrom);
            pickupTimeTo.setText(lastTimeSelectedTo);
            dateTimeLayout.setVisibility(LinearLayout.VISIBLE);
            toLayout.setVisibility(LinearLayout.VISIBLE);
        }else{
            //Set time to start/end of the actual day and save the value to restore if nedeed
            lastTimeSelectedFrom = pickupTimeFrom.getText();
            lastTimeSelectedTo = pickupTimeTo.getText();
            pickupTimeFrom.setText(formatTime.print(new DateTime().withTimeAtStartOfDay()));
            pickupTimeTo.setText(formatTime.print(new DateTime().plusDays(1).withTimeAtStartOfDay().minusMinutes(1)));
            dateTimeLayout.setVisibility(LinearLayout.GONE);
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

    /**
     * Make api call to create a new donation. It will use the from and to fields to form
     * the DateTimes needed.
     */
    public void attemptCreateDonation(){

        dateTimeFrom = getDateTimeFrom();
        dateTimeTo = getDateTimeTo();
        String description = descriptionText.getText().toString();

        progressBar.setVisibility(ProgressBar.VISIBLE);
        donationRepository.createDonation(description, dateTimeFrom.toString(),
                dateTimeTo.toString(), new RepoCallBack<Boolean>() {
            @Override
            public void onSuccess(Boolean created) {
                progressBar.setVisibility(ProgressBar.GONE);
                if(created){
                    if(onDonationCreatedListener != null){
                        onDonationCreatedListener.onDonationCreated();
                    }
                }

            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(ProgressBar.GONE);
                showCreateError();
            }
        });
    }

    public DateTime getDateTimeFrom(){
        return generateDateFromTextViews(pickupDateFrom, pickupTimeFrom);
    }

    public DateTime getDateTimeTo(){
        return generateDateFromTextViews(pickupDateTo, pickupTimeTo);
    }

    public void setDateTimeFrom(DateTime dateTimeFrom){
        pickupDateFrom.setText(formatDate.print(dateTimeFrom));
        pickupTimeFrom.setText(formatTime.print(dateTimeFrom));
    }

    public void setDateTimeTo(DateTime dateTimeFrom){
        pickupDateTo.setText(formatDate.print(dateTimeFrom));
        pickupTimeTo.setText(formatTime.print(dateTimeFrom));
    }

    private DateTime generateDateFromTextViews(TextView dateTextView, TextView timeTextView){
        return new DateTime()
                .withDate(formatDate.parseLocalDate(dateTextView.getText().toString()))
                .withTime(formatTime.parseLocalTime(timeTextView.getText().toString()));
    }

    private void showCreateError(){
        Toast.makeText(getActivity(),R.string.error_create_donation,Toast.LENGTH_SHORT);
    }


    public boolean showDatePickerDialog(final TextView view){
        DialogFragment datePickerFragment = DatePickerFragment.newInstance((year, month, day) -> {
            DateTime dateTime = new DateTime()
                    .withYear(year)
                    .withMonthOfYear(month+1) //DatePicker returns month beetween [0-11] and jodaTime use it from [1-12]
                    .withDayOfMonth(day);
            view.setText(formatDate.print(dateTime));
            validateDates();
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
            validateDates();
        });
        timePickerFragment.show(getActivity().getSupportFragmentManager(), view.toString() + "Time");
    }

    /**
     * Validate dateTimeFrom < dateTimeTo if not set dateTimeTo = dateTimeFrom + DEFAULT HOURS
     */
    private void validateDates(){
        if (getDateTimeFrom().isAfter(getDateTimeTo())){
            setDateTimeTo(getDateTimeFrom().plusHours(PLUS_HOURS_DEFAULT));
        }
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

    public void addItem(){
        itemAdapter.addItem(new Item());
    }

}

package proyectoalimentar.alimentardonanteapp.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.model.Donation;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.model.Status;
import proyectoalimentar.alimentardonanteapp.repository.DonationRepository;
import proyectoalimentar.alimentardonanteapp.repository.RepoCallBack;
import proyectoalimentar.alimentardonanteapp.ui.donations.CancelDonationView;


public class DonationView extends FrameLayout{

    @BindView(R.id.status)
    TextView stateText;
    @BindView(R.id.time)
    TextView time;
    @BindView(R.id.description)
    TextView description;
    @BindView(R.id.volunteer_information)
    LinearLayout volunteer_information;
    @BindView(R.id.cancel)
    TextView cancelDonation;
    @BindView(R.id.assigned_to)
    TextView assignedTo;


    CancelDonationView cancelDonationView;
    DateTimeFormatter formatTime = DateTimeFormat.forPattern("HH-mm");
    CancelDonationView.OnDonationCancelledCallBack onDonationCancelledCallBack;
    Donation donation;

    @Inject
    DonationRepository donationRepository;

    public DonationView(Context context) {
        super(context);
        init();
    }

    public DonationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DonationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setCancelDonationView(CancelDonationView cancelDonationView) {
        this.cancelDonationView = cancelDonationView;
        setOnCancelListener();

    }

    public void init(){
        AlimentarApp.inject(this);
        View view = inflate(getContext(), R.layout.donation, this);
        ButterKnife.bind(this,view);
        cancelDonation.setOnClickListener( (v) -> cancelDonation(donation));
    }

    public void setDonation(Donation donation){
        if(donation.getPickupTimeFrom() == null || donation.getPickupTimeTo() == null){
            throw new IllegalArgumentException();
        }
        this.donation = donation;
        refreshView();
    }

    public void refreshView(){
        //If not active, hide volunteer information
//        if(!donation.getStatus().equals(Status.ACTIVE)){
//            volunteer_information.setVisibility(LinearLayout.GONE);
//        }
        time.setText(formatTime.print(donation.getPickupTimeFrom()) +
                " - " +
                formatTime.print(donation.getPickupTimeTo()));
        if(donation.getStatus() != null) {
            stateText.setText(donation.getStatus().toString());
        }else{
            stateText.setText(Status.UNKOWN.toString());
        }
        if(donation.getDescription() != null){
            description.setText(donation.getDescription());
        }
        if(donation.getVolunteer() != null && donation.getVolunteer().getName() != null) {
            assignedTo.setText(donation.getVolunteer().getName());
        }
    }

    private void setOnCancelListener(){
        if(cancelDonationView != null) {
            cancelDonationView.setOnDonationCancelledCallBack(
                    (donation) ->{
                        Toast.makeText(getContext(), R.string.donation_cancelled, Toast.LENGTH_SHORT);
                        this.onDonationCancelledCallBack.onDonationCancelled(donation);
                    });
        }
}

    public void cancelDonation(Donation donation){
        cancelDonationView.setDonation(donation);
        if(cancelDonationView != null) {
            cancelDonationView.setVisibility(VISIBLE);
        }
    }

    public void setOnDonationCancelledCallBack(CancelDonationView.OnDonationCancelledCallBack onDonationCancelledCallBack) {
        this.onDonationCancelledCallBack = onDonationCancelledCallBack;
    }

    public Donation getDonation() {
        return donation;
    }
}

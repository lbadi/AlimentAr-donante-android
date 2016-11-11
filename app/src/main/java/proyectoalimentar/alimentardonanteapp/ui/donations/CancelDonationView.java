package proyectoalimentar.alimentardonanteapp.ui.donations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import proyectoalimentar.alimentardonanteapp.di.component.DaggerAppComponent;
import proyectoalimentar.alimentardonanteapp.di.module.AppModule;
import proyectoalimentar.alimentardonanteapp.di.module.NetworkModule;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.model.Donation;
import proyectoalimentar.alimentardonanteapp.network.DonationService;
import proyectoalimentar.alimentardonanteapp.repository.DonationRepository;
import proyectoalimentar.alimentardonanteapp.repository.RepoCallBack;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CancelDonationView extends FrameLayout{

    Donation donation;

    @Inject
    DonationRepository donationRepository;

    OnDonationCancelledCallBack onDonationCancelledCallBack;


    public CancelDonationView(Context context) {
        super(context);
    }

    public CancelDonationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CancelDonationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
            View view =
                    LayoutInflater.from(getContext()).inflate(R.layout.cancel_donation_view, this);
            ButterKnife.bind(this, view);
            DaggerAppComponent.builder()
                    .appModule(new AppModule(getContext()))
                    .networkModule(new NetworkModule())
                    .build()
                    .inject(this);

            setOnClickListener(v -> setVisibility(GONE));
    }

    @OnClick(R.id.cancel_donation)
    public void cancelDonation(){
        if(donation != null) {
            donationRepository.cancelDonation(donation, new RepoCallBack<Boolean>() {
                @Override
                public void onSuccess(Boolean canceled) {
                    if (canceled) {
                        CancelDonationView.this.setVisibility(GONE);
                        if (onDonationCancelledCallBack != null) {
                            onDonationCancelledCallBack.onDonationCancelled();
                        }
                    } else {
                        onError(null);
                    }
                }

                @Override
                public void onError(String error) {
                    CancelDonationView.this.setVisibility(GONE);
                    Toast.makeText(getContext(), R.string.cancel_error, Toast.LENGTH_SHORT);
                }
            });
        }
    }

    @OnClick(R.id.go_back)
    public void goBack(){
        this.setVisibility(FrameLayout.GONE);
    }

    public interface OnDonationCancelledCallBack{

        public void onDonationCancelled();
    }

    public void setDonation(Donation donation) {
        this.donation = donation;
    }

    public void setOnDonationCancelledCallBack(OnDonationCancelledCallBack onDonationCancelledCallBack) {
        this.onDonationCancelledCallBack = onDonationCancelledCallBack;
    }
}
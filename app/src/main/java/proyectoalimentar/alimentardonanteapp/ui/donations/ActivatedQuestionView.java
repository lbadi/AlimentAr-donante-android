package proyectoalimentar.alimentardonanteapp.ui.donations;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import proyectoalimentar.alimentardonanteapp.R;
import proyectoalimentar.alimentardonanteapp.di.component.DaggerAppComponent;
import proyectoalimentar.alimentardonanteapp.di.module.AppModule;
import proyectoalimentar.alimentardonanteapp.di.module.NetworkModule;
import proyectoalimentar.alimentardonanteapp.model.Donation;
import proyectoalimentar.alimentardonanteapp.repository.DonationRepository;
import proyectoalimentar.alimentardonanteapp.repository.RepoCallBack;

public class ActivatedQuestionView extends FrameLayout {

    Integer donationId;
    String userName;

    @BindView(R.id.question_text)
    TextView questionTextView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;


    @Inject
    DonationRepository donationRepository;

    OnResponseCallback onResponseCallback;

    public ActivatedQuestionView(Context context) {
        super(context);
    }

    public ActivatedQuestionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActivatedQuestionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        View view =
                LayoutInflater.from(getContext()).inflate(R.layout.donation_activated_question, this);
        ButterKnife.bind(this, view);
        DaggerAppComponent.builder()
                .appModule(new AppModule(getContext()))
                .networkModule(new NetworkModule())
                .build()
                .inject(this);

        setOnClickListener(v -> setVisibility(GONE));
    }

    private void fillQuestionText(){
        String question = getResources().getString(R.string.activated_question);
        question = String.format(question,userName);
        questionTextView.setText(question);
    }

    @OnClick(R.id.yes)
    public void confirmActivation(){
        if(donationId != null) {
            progressBar.setVisibility(VISIBLE);
            donationRepository.onGoingDonation(donationId, new RepoCallBack<Boolean>() {
                @Override
                public void onSuccess(Boolean answered) {
                    if (answered) {
                        ActivatedQuestionView.this.setVisibility(GONE);
                        if (onResponseCallback != null) {
                            onResponseCallback.onResponse(true);
                        }
                    } else {
                        onError(null);
                    }
                }

                @Override
                public void onError(String error) {
                    ActivatedQuestionView.this.setVisibility(GONE);
                    Toast.makeText(getContext(), R.string.cancel_error, Toast.LENGTH_SHORT);
                }
            });
        }
    }

    @OnClick(R.id.no)
    public void DeniedActivation(){
        if(donationId != null) {
            progressBar.setVisibility(VISIBLE);
            donationRepository.openDonation(donationId, new RepoCallBack<Boolean>() {
                @Override
                public void onSuccess(Boolean answered) {
                    progressBar.setVisibility(GONE);
                    if (answered) {
                        ActivatedQuestionView.this.setVisibility(GONE);
                        if (onResponseCallback != null) {
                            onResponseCallback.onResponse(false);
                        }
                    } else {
                        onError(null);
                    }
                }

                @Override
                public void onError(String error) {
                    progressBar.setVisibility(GONE);
                    ActivatedQuestionView.this.setVisibility(GONE);
                    Toast.makeText(getContext(), R.string.cancel_error, Toast.LENGTH_SHORT);
                }
            });
        }
        this.setVisibility(FrameLayout.GONE);
    }

    public void setInformation(String donationId, String userName) {
        try {
            this.donationId = Integer.valueOf(donationId);
            this.userName = userName;
        }catch (NumberFormatException e){
            throw new IllegalArgumentException();
        }
        fillQuestionText();
    }

    public void setOnResponseCallback(OnResponseCallback onResponseCallback) {
        this.onResponseCallback = onResponseCallback;
    }

    public interface OnResponseCallback{
        void onResponse(boolean confirmActivation);
    }
}

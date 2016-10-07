package proyectoalimentar.alimentardonanteapp.donations;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import proyectoalimentar.alimentardonanteapp.R;

import proyectoalimentar.alimentardonanteapp.donations.DonationFragment.OnDonationClickListener;
import proyectoalimentar.alimentardonanteapp.model.Donation;

public class DonationsActivity extends AppCompatActivity implements OnDonationClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donations);
    }

    @Override
    public void onDonationClick(Donation item) {

    }
}

package proyectoalimentar.alimentardonanteapp.ui.donations;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.R;


import proyectoalimentar.alimentardonanteapp.model.Donation;
import proyectoalimentar.alimentardonanteapp.ui.view.DonationView;

import java.util.LinkedList;
import java.util.List;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.ViewHolder> {

    private final List<Donation> donations;
    CancelDonationView cancelDonationView;

    public DonationAdapter(CancelDonationView cancelDonationView){
        donations = new LinkedList<>();
        this.cancelDonationView = cancelDonationView;
    }

    public void setDonations(List donations){
        this.donations.clear();
        this.donations.addAll(donations);
        notifyDataSetChanged();
    }

    public void removeDonation(Donation donation){
        this.donations.remove(donation);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.donation_fragment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Donation donation = donations.get(position);
        holder.populateWithDonation(donation,cancelDonationView);
    }

    @Override
    public int getItemCount() {
        return donations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.donation)
        public DonationView donationView;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        void populateWithDonation(Donation donation, CancelDonationView cancelDonationView){
            donationView.setDonation(donation);
            donationView.setOnDonationCancelledCallBack((d) -> {
                removeDonation(d);
            });
            donationView.setCancelDonationView(cancelDonationView);
        }
    }
}

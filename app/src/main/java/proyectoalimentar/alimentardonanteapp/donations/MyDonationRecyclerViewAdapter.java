package proyectoalimentar.alimentardonanteapp.donations;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.R;


import proyectoalimentar.alimentardonanteapp.donations.DonationFragment.OnDonationClickListener;
import proyectoalimentar.alimentardonanteapp.model.Donation;

import java.text.DateFormat;
import java.util.List;

public class MyDonationRecyclerViewAdapter extends RecyclerView.Adapter<MyDonationRecyclerViewAdapter.ViewHolder> {

    private final List<Donation> mValues;
    private final DonationFragment.OnDonationClickListener mListener;

    public MyDonationRecyclerViewAdapter(List<Donation> items, OnDonationClickListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_donation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.donation = mValues.get(position);
        holder.stateView.setText(holder.donation.getState().toString());
//        holder.pickupTimeToView.setText(DateFormat.getDateTimeInstance().format(holder.donation.getPickupTimeTo().getTime()));
//        holder.pickupTimeFromView.setText(DateFormat.getDateTimeInstance().format(holder.donation.getPickupTimeFrom().toString()));
        holder.assignToView.setText("Juan");

        if((position % 2) == 1){
            holder.rowView.setBackgroundColor(ContextCompat.getColor(AlimentarApp.getContext(), R.color.colorPrimary));
            holder.assignToView.setTextColor(ContextCompat.getColor(AlimentarApp.getContext(), R.color.colorAccent));
        }else{
            holder.rowView.setBackgroundColor(ContextCompat.getColor(AlimentarApp.getContext(), R.color.colorAccent));
            holder.assignToView.setTextColor(ContextCompat.getColor(AlimentarApp.getContext(), R.color.colorPrimary));

        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onDonationClick(holder.donation);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final View rowView;
        public final TextView stateView;
//        public final TextView pickupTimeFromView;
//        public final TextView pickupTimeToView;
        public final TextView assignToView;

        public Donation donation;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            stateView = (TextView) view.findViewById(R.id.state);
//            pickupTimeToView = (TextView) view.findViewById(R.id.pickup_time_to);
//            pickupTimeFromView = (TextView) view.findViewById(R.id.pickup_time_from);
            assignToView = (TextView) view.findViewById(R.id.assign_to);
            rowView =  view.findViewById(R.id.row_layout);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + stateView.getText() + "'";
        }
    }
}

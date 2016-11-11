package proyectoalimentar.alimentardonanteapp.ui.donations;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import proyectoalimentar.alimentardonanteapp.AlimentarApp;
import proyectoalimentar.alimentardonanteapp.Configuration;
import proyectoalimentar.alimentardonanteapp.R;

import proyectoalimentar.alimentardonanteapp.network.DonationService;
import proyectoalimentar.alimentardonanteapp.repository.DonationRepository;
import proyectoalimentar.alimentardonanteapp.repository.RepoCallBack;
import proyectoalimentar.alimentardonanteapp.model.Donation;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerActivity;
import proyectoalimentar.alimentardonanteapp.ui.drawer.DrawerItem;
import proyectoalimentar.alimentardonanteapp.utils.StorageUtils;

/**
 * A fragment representing a list of donations.
 */
public class DonationFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.donation_list)
    RecyclerView donationList;
    @BindView(R.id.new_donation_button)
    View newDonationButton;
    @BindView(R.id.cancel_donation_view)
    CancelDonationView cancelDonationView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    DonationRepository donationRepository;

    DonationAdapter donationAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        AlimentarApp.inject(this);
        View view = inflater.inflate(R.layout.donation_list_fragment, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init(){
        setupDrawer();
        setupDonations();
        setupSwipeToRefresh();
        newDonationButton.setOnClickListener( v -> newDonation());
    }

    private void setupDrawer() {
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        toolbar.setNavigationOnClickListener(v -> ((DrawerActivity) getActivity()).toggleDrawer());
    }

    private void setupDonations(){
        donationAdapter = new DonationAdapter(cancelDonationView);
        donationList.setAdapter(donationAdapter);
        donationList.setLayoutManager(new LinearLayoutManager(getActivity()));
        swipeRefreshLayout.setRefreshing(true);
        updateDonations();
    }

    private void setupSwipeToRefresh(){
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
//        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            updateDonations();
        });
    }

    /**
     * Update donation list
     */
    private void updateDonations(){
        donationRepository.activeAndOpenDonations(new RepoCallBack<List<Donation>>() {
            @Override
            public void onSuccess(List<Donation> donations) {
                donationAdapter.setDonations(donations);
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onError(String error) {
                if(isAdded() && getActivity() != null) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.error_fetching_donations),
                            Toast.LENGTH_SHORT).show();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });
    }

    private void newDonation(){
        if(getActivity() instanceof DrawerActivity){
            DrawerActivity drawerActivity = (DrawerActivity) getActivity();
            drawerActivity.openDrawerItem(DrawerItem.NEW_DONATION);
        }
    }
}

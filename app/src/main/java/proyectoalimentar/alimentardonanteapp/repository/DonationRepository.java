package proyectoalimentar.alimentardonanteapp.repository;


import org.joda.time.DateTime;

import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import proyectoalimentar.alimentardonanteapp.Configuration;
import proyectoalimentar.alimentardonanteapp.model.AuthenticatedUser;
import proyectoalimentar.alimentardonanteapp.model.Donation;
import proyectoalimentar.alimentardonanteapp.model.Donator;
import proyectoalimentar.alimentardonanteapp.model.Item;
import proyectoalimentar.alimentardonanteapp.model.Qualification;
import proyectoalimentar.alimentardonanteapp.network.DonationService;
import proyectoalimentar.alimentardonanteapp.utils.StorageUtils;
import proyectoalimentar.alimentardonanteapp.utils.UserStorage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class DonationRepository {

    @Inject
    DonationService donationService;
    @Inject
    UserStorage userStorage;

    @Inject
    public DonationRepository(){

    }

    public void createDonation(String description, DateTime pickupTimeFrom,
                               DateTime pickUpTimeTo, List<Item> items, RepoCallBack<Boolean> repoCallBack){
        Donation donation = new Donation(null,null,description, pickupTimeFrom, pickUpTimeTo, null);
        donation.addItems(items);
        donationService.create(donation).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                repoCallBack.onSuccess(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                repoCallBack.onError(t.getMessage());
            }
        });
    }

    public void cancelDonation(Donation donation, RepoCallBack<Boolean> repoCallBack){
        donationService.cancel(donation.getId()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                repoCallBack.onSuccess(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                repoCallBack.onError(t.getMessage());
            }
        });
    }

    public void onGoingDonation(Integer donationId, RepoCallBack<Boolean> repoCallBack){
        donationService.onGoing(donationId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                repoCallBack.onSuccess(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                repoCallBack.onError(t.getMessage());
            }
        });
    }

    public void qualifyDonation(Integer donationId, Qualification qualification, RepoCallBack<Boolean> repoCallBack){
        donationService.qualify(donationId, qualification).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                repoCallBack.onSuccess(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                repoCallBack.onError(t.getMessage());
            }
        });
    }

    public void openDonation(Integer donationId, RepoCallBack<Boolean> repoCallBack){
        donationService.open(donationId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                repoCallBack.onSuccess(response.isSuccessful());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                repoCallBack.onError(t.getMessage());
            }
        });
    }


    public void activeDonations(RepoCallBack<List<Donation>> repoCallBack){
        donationService.listActive().enqueue(new Callback<List<Donation>>() {
            @Override
            public void onResponse(Call<List<Donation>> call, Response<List<Donation>> response) {
                if(response.isSuccessful()){
                    repoCallBack.onSuccess(response.body());
                }else {
                    repoCallBack.onError(null);
                }
            }

            @Override
            public void onFailure(Call<List<Donation>> call, Throwable t) {
                repoCallBack.onError(t.getMessage());
            }
        });
    }

    public void openDonations(RepoCallBack<List<Donation>> repoCallBack){
        donationService.listOpen().enqueue(new Callback<List<Donation>>() {
            @Override
            public void onResponse(Call<List<Donation>> call, Response<List<Donation>> response) {
                if(response.isSuccessful()){
                    repoCallBack.onSuccess(response.body());
                }else {
                    repoCallBack.onError(null);
                }
            }

            @Override
            public void onFailure(Call<List<Donation>> call, Throwable t) {
                repoCallBack.onError(t.getMessage());
            }
        });
    }

    public void activeAndOpenDonations(RepoCallBack<List<Donation>> repoCallBack){
        List<Donation> donations = new LinkedList<>();
        donationService.listOpen().enqueue(new Callback<List<Donation>>() {
            @Override
            public void onResponse(Call<List<Donation>> call, Response<List<Donation>> response) {
                if(response.isSuccessful()){
                    donations.addAll(response.body());
                    donationService.listActive().enqueue(new Callback<List<Donation>>() {
                        @Override
                        public void onResponse(Call<List<Donation>> call, Response<List<Donation>> response) {
                            if(response.isSuccessful()){
                                donations.addAll(response.body());
                                //Store in shared preferences so we can have a list of last donations
                                //in our watcher service.
                                StorageUtils.storeInSharedPreferences(Configuration.LAST_DONATIONS,donations);
                                repoCallBack.onSuccess(donations);
                            }else{
                                repoCallBack.onError(null);
                            }
                        }
                        @Override
                        public void onFailure(Call<List<Donation>> call, Throwable t) {
                            repoCallBack.onError(t.getMessage());
                        }
                    });
                }else {
                    repoCallBack.onError(null);
                }
            }

            @Override
            public void onFailure(Call<List<Donation>> call, Throwable t) {
                repoCallBack.onError(t.getMessage());
            }
        });
    }


    //TODO
//    private List<Donation> filterMyDonations(List<Donation> donations){
//        AuthenticatedUser user = userStorage.getLoggedUser();
//        if(user.getEmail() != null){
//            donations.st.filter(donation -> user.getEmail().equals(donation.getDonator().getEmail()));
//        }
//    }
}

package proyectoalimentar.alimentardonanteapp.network;

import java.util.List;

import proyectoalimentar.alimentardonanteapp.model.Donation;
import proyectoalimentar.alimentardonanteapp.model.Donator;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DonationService {

    @GET("donations/active")
    Call<List<Donation>> listActive();

    @GET("donations/open")
    Call<List<Donation>> listOpen();

    @GET("donations/finished")
    Call<List<Donation>> listFinished();

    @POST("donations")
    @FormUrlEncoded
    Call<Void> create(@Field("description") String description,
                          @Field("pickup_time_from") String pickUpTimeFrom,
                          @Field("pickup_time_to") String pickUpTimeTo);

    @POST("donations/{id}/cancel")
    Call<Void> cancel(@Path("id") Integer id);

    @POST("donations/{id}/ongoing")
    Call<Void> onGoing(@Path("id") Integer id);

    @POST("donations/{id}/reopen")
    Call<Void> open(@Path("id") Integer id);


}

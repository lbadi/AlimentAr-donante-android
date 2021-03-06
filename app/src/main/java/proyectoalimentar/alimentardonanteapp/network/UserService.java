package proyectoalimentar.alimentardonanteapp.network;

import okhttp3.RequestBody;
import proyectoalimentar.alimentardonanteapp.model.AuthenticatedUser;
import proyectoalimentar.alimentardonanteapp.model.Donator;
import proyectoalimentar.alimentardonanteapp.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {


    @POST("o_auth/token")
    @FormUrlEncoded
    Call<AuthenticatedUser> login(@Field("email") String email, @Field("password") String password);

    @POST("donators")
    @FormUrlEncoded
    Call<AuthenticatedUser> signUp(@Field("email") String email,
                                   @Field("password") String password,
                                   @Field("name") String name,
                                   @Field("address") String address);

    @GET("donators/me")
    Call<Donator> getMyInformation();

    @PUT("donators/{id}")
    @FormUrlEncoded
    Call<Void> update(@Path("id") Integer id,@Field("name") String name,@Field("address") String address);

    @Multipart
    @PUT("donators/{id}")
    Call<Void> updateAvatar(@Path("id") int userId,
                            @Part("avatar; filename=avatar.jpeg ") RequestBody photo);

}

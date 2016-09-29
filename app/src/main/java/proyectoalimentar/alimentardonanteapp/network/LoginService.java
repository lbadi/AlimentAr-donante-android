package proyectoalimentar.alimentardonanteapp.network;

import proyectoalimentar.alimentardonanteapp.model.AuthenticatedUser;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface LoginService {


    @POST("o_auth/token")
    @FormUrlEncoded
    Call<AuthenticatedUser> login(@Field("email") String email, @Field("password") String password);

    @POST("donators")
    @FormUrlEncoded
    Call<AuthenticatedUser> signUp(@Field("email") String email,
                                   @Field("password") String password,
                                   @Field("password_confirmation") String passwordConfirmation,
                                   @Field("name") String name,
                                   @Field("address") String address);
}

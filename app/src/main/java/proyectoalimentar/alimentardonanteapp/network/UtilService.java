package proyectoalimentar.alimentardonanteapp.network;

import java.util.List;

import proyectoalimentar.alimentardonanteapp.model.Donation;
import proyectoalimentar.alimentardonanteapp.model.ProductType;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by leonelbadi on 6/4/17.
 */

public interface UtilService {

    @GET("product_types")
    Call<List<ProductType>> listProductType();
}

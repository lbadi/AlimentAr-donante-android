package proyectoalimentar.alimentardonanteapp.repository;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import proyectoalimentar.alimentardonanteapp.Configuration;
import proyectoalimentar.alimentardonanteapp.model.ProductType;
import proyectoalimentar.alimentardonanteapp.network.UtilService;
import proyectoalimentar.alimentardonanteapp.utils.StorageUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for generic endpoints.
 */

public class UtilRepository implements CachedRepository{

    @Inject
    UtilService utilService;

    @Inject
    public UtilRepository(){
//        initCache();
    }

    /**
     * Get a list of product types.
     * Get from shared preference if present. If not, call the api and store it in shared preferences.
     * @param repoCallBack
     */
    public void listProductTypes(RepoCallBack<List<ProductType>> repoCallBack){
        Type listProductType = new TypeToken<ArrayList<ProductType>>(){}.getType();

        List<ProductType> productType = StorageUtils.getObjectFromSharedPreferences(Configuration.PRODUCT_TYPES, listProductType);
//        if(productType != null){
//            repoCallBack.onSuccess(productType);
//        }else{
            utilService.listProductType().enqueue(new Callback<List<ProductType>>() {
                @Override
                public void onResponse(Call<List<ProductType>> call, Response<List<ProductType>> response) {
                    if(response.isSuccessful()){
                        StorageUtils.storeInSharedPreferences(Configuration.PRODUCT_TYPES, response.body());
                        repoCallBack.onSuccess(response.body());
                    }else{
                        repoCallBack.onError(null);
                    }
                }

                @Override
                public void onFailure(Call<List<ProductType>> call, Throwable t) {
                    repoCallBack.onError(t.getMessage());
                }
            });
//        }
    }

    @Override
    public void initCache() {
        listProductTypes(new RepoCallBack<List<ProductType>>() {
            @Override
            public void onSuccess(List<ProductType> value) {

            }

            @Override
            public void onError(String error) {

            }
        });
    }
}

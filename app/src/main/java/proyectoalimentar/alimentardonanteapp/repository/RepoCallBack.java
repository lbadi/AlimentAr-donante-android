package proyectoalimentar.alimentardonanteapp.repository;

/**
 * Created by leonelbadi on 28/10/16.
 */
public interface RepoCallBack<T> {

    public void onSuccess(T value);

    public void onError(String error);
}

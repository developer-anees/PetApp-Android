package in.anees.petapp.ui.listeners;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public interface NetworkRequestResponseListener {

    public void onResponseSuccess(Object object);
    public void onResponseFailure(Object object);
}

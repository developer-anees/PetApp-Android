package in.anees.petapp.data.network.api;

import in.anees.petapp.data.network.networkmodel.ConfigurationSchema;
import in.anees.petapp.data.network.networkmodel.PetsSchema;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by Anees Thyrantakath on 2019-08-03.
 */
public interface PetAppApi {
    @GET
    Call<PetsSchema> getPetList(@Url String completeUrl);

    @GET
    Call<ConfigurationSchema> getConfiguration(@Url String completeUrl);
}

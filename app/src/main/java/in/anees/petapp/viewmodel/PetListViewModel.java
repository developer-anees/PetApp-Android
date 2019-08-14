package in.anees.petapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import in.anees.petapp.common.Constants;
import in.anees.petapp.data.model.Configuration;
import in.anees.petapp.data.network.PetAppNetworkService;
import in.anees.petapp.data.network.networkmodel.ConfigurationSchema;
import in.anees.petapp.data.network.networkmodel.PetsSchema;
import in.anees.petapp.viewmodel.apiresponse.ConfigurationResponse;
import in.anees.petapp.viewmodel.apiresponse.PetListResponse;
import in.anees.petapp.utils.DateUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anees Thyrantakath on 2019-08-14.
 */
public class PetListViewModel extends AndroidViewModel {
    private MutableLiveData<PetListResponse> mPetList;
    private MutableLiveData<ConfigurationResponse> mConfiguration;

    private PetAppNetworkService mPetAppNetworkService;


    public PetListViewModel(@NonNull Application application) {
        super(application);
        mPetAppNetworkService = new PetAppNetworkService(application);
    }

    public LiveData<PetListResponse> getPets() {
        if (mPetList == null) {
            mPetList = new MutableLiveData<PetListResponse>();
            handleFetchPetList();
        }
        return mPetList;
    }

    public void handleFetchPetList() {
        mPetAppNetworkService.getPetAppApi().getPetList(Constants.PET_LIST_URL).enqueue(new Callback<PetsSchema>() {
            @Override
            public void onResponse(Call<PetsSchema> call, Response<PetsSchema> response) {
                if (response.isSuccessful()) {
                    notifyFetchPetListSuccess(response.body());
                } else {
                    notifyFetchPetListError(String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<PetsSchema> call, Throwable t) {
                notifyFetchPetListError(t.getMessage());
            }
        });
    }

    private void notifyFetchPetListError(String errorMessage) {
        mPetList.setValue(new PetListResponse(errorMessage));

    }

    private void notifyFetchPetListSuccess(PetsSchema petsSchema) {
        List<in.anees.petapp.data.network.networkmodel.Pet> petsInSchema = petsSchema.getPets();
        List<in.anees.petapp.data.model.Pet> petList = new ArrayList<>();

        for (in.anees.petapp.data.network.networkmodel.Pet pet : petsInSchema) {
            petList.add(new in.anees.petapp.data.model.Pet(
                    pet.getContentUrl(),
                    pet.getDateAdded(),
                    pet.getImageUrl(),
                    pet.getTitle()
            ));
        }
       mPetList.postValue(new PetListResponse(petList));
    }

    public LiveData<ConfigurationResponse> getConfiguration() {
        if (mConfiguration == null) {
            mConfiguration = new MutableLiveData<ConfigurationResponse>();
            handleFetchConfiguration();
        }
        return mConfiguration;
    }

    private void handleFetchConfiguration() {
        mPetAppNetworkService.getPetAppApi().getConfiguration(Constants.CONFIG_URL).enqueue(new Callback<ConfigurationSchema>() {
            @Override
            public void onResponse(Call<ConfigurationSchema> call, Response<ConfigurationSchema> response) {
                if (response.isSuccessful()){
                    try {
                        notifyConfigurationSuccess(response.body());
                    } catch (ParseException e) {
                        notifyConfigurationError(e.getMessage());
                    }
                } else {
                    notifyConfigurationError(String.valueOf(response.code()));
                }
            }

            @Override
            public void onFailure(Call<ConfigurationSchema> call, Throwable t) {
                notifyConfigurationError(t.getMessage());
            }
        });
    }

    private void notifyConfigurationError(String errorMessage) {
        mConfiguration.setValue(new ConfigurationResponse(errorMessage));
    }

    private void notifyConfigurationSuccess(ConfigurationSchema configurationSchema) throws ParseException {
        Configuration configuration = new Configuration(
                configurationSchema.getSettings().getIsChatEnabled(),
                configurationSchema.getSettings().getIsCallEnabled(),
                configurationSchema.getSettings().getWorkHours(),
                DateUtils.getWorkingTimings(configurationSchema.getSettings().getWorkHours())

        );
        mConfiguration.postValue(new ConfigurationResponse(configuration));
    }
}

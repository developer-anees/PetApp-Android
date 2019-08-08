package in.anees.petapp.presenter;

import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.anees.petapp.common.Constants;
import in.anees.petapp.model.Configuration;
import in.anees.petapp.model.WorkingTime;
import in.anees.petapp.network.PetAppNetworkService;
import in.anees.petapp.network.networkmodel.ConfigurationSchema;
import in.anees.petapp.network.networkmodel.Pet;
import in.anees.petapp.network.networkmodel.PetsSchema;
import in.anees.petapp.ui.fragment.PetListFragment;
import in.anees.petapp.utils.DateUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Anees Thyrantakath on 2019-08-03.
 */
public class PetListPresenter implements PetListContract.PetListMvpPresenter {
    private static final String TAG = "PetListPresenter";

    private PetAppNetworkService mPetAppNetworkService;
    private PetListContract.PetListMvpView mPetListMvpView;

    public PetListPresenter(PetListFragment petListFragment) {
        mPetListMvpView = petListFragment;
        mPetAppNetworkService = new PetAppNetworkService(petListFragment.getContext().getApplicationContext());
    }

    @Override
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
        mPetListMvpView.setErrorWhileFetchingPetList(errorMessage);

    }

    private void notifyFetchPetListSuccess(PetsSchema petsSchema) {
        List<Pet> petsInSchema = petsSchema.getPets();
        List<in.anees.petapp.model.Pet> petList = new ArrayList<>();

        for (Pet pet : petsInSchema) {
            petList.add(new in.anees.petapp.model.Pet(
                    pet.getContentUrl(),
                    pet.getDateAdded(),
                    pet.getImageUrl(),
                    pet.getTitle()
            ));
        }

        mPetListMvpView.setPetListValuesSuccess(petList);
    }

    @Override
    public void handleFetchConfiguration() {
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

    @Override
    public void handleCallOrChatButtonClick(Configuration configuration) {
        boolean isThisTheRightTime = false;
        if (configuration != null) {
            WorkingTime workingTime = configuration.getWorkingTime();
            isThisTheRightTime = DateUtils
                    .isWithinWorkingHours(new Date(), workingTime.getOpeningTime(), workingTime.getClosingTime());
        }
        Log.i(TAG, "Is pet shop opened? : " + isThisTheRightTime);
        mPetListMvpView.displayAlertDialogWithMessage(isThisTheRightTime, false);
    }

    private void notifyConfigurationError(String errorMessage) {
        mPetListMvpView.setErrorFetchingConfiguration(errorMessage);
    }

    private void notifyConfigurationSuccess(ConfigurationSchema configurationSchema) throws ParseException {
        Configuration configuration = new Configuration(
                configurationSchema.getSettings().getIsChatEnabled(),
                configurationSchema.getSettings().getIsCallEnabled(),
                configurationSchema.getSettings().getWorkHours(),
                DateUtils.getWorkingTimings(configurationSchema.getSettings().getWorkHours())

        );

        mPetListMvpView.setSuccessConfiguration(configuration);
    }

}

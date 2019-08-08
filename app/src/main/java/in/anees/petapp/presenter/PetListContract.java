package in.anees.petapp.presenter;

import java.util.List;

import in.anees.petapp.model.Configuration;
import in.anees.petapp.model.Pet;

/**
 * Created by Anees Thyrantakath on 2019-08-07.
 */
public class PetListContract {

    public interface PetListMvpView {
        void setSuccessConfiguration(Configuration configuration);
        void setErrorFetchingConfiguration(String errorMessage);

        void setPetListValuesSuccess(List<Pet> petList);
        void setErrorWhileFetchingPetList(String errorMessage);

        void displayAlertDialogWithMessage(boolean isThisTheRightTime, boolean isFinishOkToFinishApp);
    }

    interface PetListMvpPresenter {
        void handleFetchPetList();
        void handleFetchConfiguration();
        void handleCallOrChatButtonClick(Configuration configuration);
    }
}

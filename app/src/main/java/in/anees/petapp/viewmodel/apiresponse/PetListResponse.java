package in.anees.petapp.viewmodel.apiresponse;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import in.anees.petapp.data.model.Pet;

/**
 * Created by Anees Thyrantakath on 2019-08-14.
 */
public class PetListResponse extends BaseResponse<List<Pet>> {

    public PetListResponse(State state,@Nullable List<Pet> pets,@Nullable String error) {
        this.currentState = state;
        this.data = pets;
        this.error = null;
    }

    public static PetListResponse ERROR_STATE = new PetListResponse( State.FAILED, null, "");
    public static PetListResponse LOADING_STATE = new PetListResponse( State.LOADING, null, null);
    public static PetListResponse SUCCESS_STATE = new PetListResponse( State.SUCCESS, new ArrayList<Pet>(), null);
}

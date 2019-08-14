package in.anees.petapp.viewmodel.apiresponse;

import java.util.List;

import in.anees.petapp.data.model.Pet;

/**
 * Created by Anees Thyrantakath on 2019-08-14.
 */
public class PetListResponse {
    public List<Pet> petList;
    private String message;

    public PetListResponse(List<Pet> pets) {
        this.petList = pets;
        this.message = null;
    }

    public PetListResponse(String error) {
        this.message = error;
        this.petList = null;
    }

    public List<Pet> getPetList() {
        return petList;
    }

    public void setPetList(List<Pet> petList) {
        this.petList = petList;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

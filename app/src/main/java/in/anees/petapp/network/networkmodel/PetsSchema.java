
package in.anees.petapp.network.networkmodel;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class PetsSchema {
    @SerializedName("pets")
    private List<Pet> mPets;

    public List<Pet> getPets() {
        return mPets;
    }

    public void setPets(List<Pet> pets) {
        mPets = pets;
    }
}

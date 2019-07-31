
package in.anees.petapp.model;

import java.util.List;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public class PetDetails {

    private List<Pet> mPets;

    public List<Pet> getPets() {
        return mPets;
    }

    public PetDetails setPets(List<Pet> pets) {
        mPets = pets;
        return this;
    }
}

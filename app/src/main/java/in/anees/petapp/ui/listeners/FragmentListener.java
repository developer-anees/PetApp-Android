package in.anees.petapp.ui.listeners;

import androidx.fragment.app.Fragment;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public interface FragmentListener {

    public void addFragment(final Fragment fragment, boolean addToBackStack, String tag);
}

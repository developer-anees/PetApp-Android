package in.anees.petapp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import in.anees.petapp.R;
import in.anees.petapp.ui.fragment.PetListFragment;
import in.anees.petapp.ui.listeners.FragmentListener;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public class MainActivity extends FragmentActivity implements FragmentListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            addFragment(PetListFragment.newInstance(), false, PetListFragment.TAG);
        }
    }

    @Override
    public void addFragment(final Fragment fragment, boolean addToBackStack, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.main_container, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(tag);
        } else {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }
}

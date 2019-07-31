package in.anees.petapp.ui.fragment;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Date;

import in.anees.petapp.R;
import in.anees.petapp.constant.Constants;
import in.anees.petapp.model.Configuration;
import in.anees.petapp.model.Pet;
import in.anees.petapp.model.PetDetails;
import in.anees.petapp.model.WorkingTime;
import in.anees.petapp.network.JsonRequestAsyncTask;
import in.anees.petapp.ui.adapter.PetListAdapter;
import in.anees.petapp.ui.listeners.NetworkRequestResponseListener;
import in.anees.petapp.utils.CommonAlert;
import in.anees.petapp.utils.DateUtils;
import in.anees.petapp.utils.NetworkUtils;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 * TODO: Not actively listening to network state change now.
 */
public class PetListFragment extends BaseFragment implements NetworkRequestResponseListener, View.OnClickListener {

    public static String TAG_PET_LIST = "PET_LIST";

    private Button mButtonChat, mButtonCall;
    private TextView mTvWorkingHours,tvEmptyText;
    private ListView mPetsListView;

    private PetDetails mPetDetails;
    private Configuration mConfiguration;

    public PetListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment
     * @return A new instance of fragment PetListFragment.
     */
    public static PetListFragment newInstance() {
        return new PetListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pet_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mButtonChat = (Button) view.findViewById(R.id.buttonChat);
        mButtonChat.setOnClickListener(this);
        mButtonCall = (Button) view.findViewById(R.id.buttonCall);
        mButtonCall.setOnClickListener(this);

        mTvWorkingHours = (TextView) view.findViewById(R.id.tvWorkingHours);
        mPetsListView = (ListView) view.findViewById(R.id.listViewPets);
        tvEmptyText = (TextView) view.findViewById(R.id.tvEmptyText);
        setEmptyTextViewToList(getString(R.string.loading));
        mPetsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pet pet = (Pet) mPetsListView.getItemAtPosition(position);
                Log.i(TAG_PET_LIST, "Selected : " + pet.getTitle());
                if (!NetworkUtils.isNetworkConnected(mContext)) {
                    displayDialog(getString(R.string.no_network), false);
                    return;
                }
                PetDetailsFragment petDetailsFragment
                        = PetDetailsFragment.newInstance(pet.getTitle(), pet.getContentUrl());
                mListener.addFragment(petDetailsFragment, false, PetDetailsFragment.TAG_PET_DETAILS);
            }
        });

        // Load if network is there
        if (NetworkUtils.isNetworkConnected(mContext)) {
            Log.i(TAG_PET_LIST, "Network connected loading information!");
            // Handle situation if No network Dialog is displayed and then user activate mobile data and try
            if (mConfiguration == null || mPetDetails == null) {
                AlertDialogFragment fragment = isAlertDialogFragmentInBackStack();
                if (fragment != null) {
                    boolean isOkToExit = fragment.getArguments().getBoolean(AlertDialogFragment.ARG_PARAM2);
                    if (isOkToExit) fragment.dismiss();
                }
            }
            loadInformation();
            return;
        }

        // Check whether data is already exist if exist load and display.
        if (savedInstanceState != null) {
            if (mConfiguration!=null) {
                onResponseSuccess(mConfiguration);
            }
            if (mPetDetails!=null) {
                onResponseSuccess(mPetDetails);
            }

            // If both are not null then it's okay not to show "No internet dialog"
            if (mConfiguration != null && mPetDetails != null){
               return;
            }
        }
        displayDialog(getString(R.string.no_network), true);
    }

    @Override
    public void onClick(View v) {
        boolean isThisTheRightTime = false;
        if (mConfiguration != null) {
            WorkingTime workingTime = mConfiguration.getWorkingTime();
            isThisTheRightTime = DateUtils
                    .isWithinWorkingHours(new Date(), workingTime.getOpeningTime(), workingTime.getClosingTime());
        }
        Log.i(TAG_PET_LIST, "Is pet shop opened? : " + isThisTheRightTime);
        switch (v.getId()) {
            case R.id.buttonChat:
            case R.id.buttonCall:
                displayDialog((isThisTheRightTime) ?
                        getString(R.string.within_work_hours) : getString(R.string.outside_work_hours),false);
                break;
        }
    }

    @Override
    public void onResponseSuccess(Object object) {
        Log.i(TAG_PET_LIST, "updateButtons Object : " + object.getClass().getName());
        if (object instanceof Configuration) {
            mConfiguration = (Configuration) object;

            mButtonCall.setVisibility(mConfiguration.isIsCallEnabled() ? View.VISIBLE : View.GONE);
            mButtonChat.setVisibility(mConfiguration.isIsChatEnabled() ? View.VISIBLE : View.GONE);
            String workingHours = mConfiguration.getWorkHours();
            if (!workingHours.isEmpty()) {
                mTvWorkingHours.setVisibility(View.VISIBLE);
                mTvWorkingHours.setText(workingHours);
            }
        } else if (object instanceof PetDetails) {
            mPetDetails = (PetDetails) object;
            ArrayList<Pet> listPetDetails = (ArrayList<Pet>) mPetDetails.getPets();
            if (listPetDetails!=null && listPetDetails.size() == 0) {
                setEmptyTextViewToList(getString(R.string.no_pet_info));
            }
            PetListAdapter customAdapter = new PetListAdapter(mContext, listPetDetails);
            mPetsListView.setAdapter(customAdapter);
        }
    }

    @Override
    public void onResponseFailure(Object object) {
        CommonAlert.showToast(mContext, "Failed to get response from server!");
        Log.i(TAG_PET_LIST, "isNetworkConnected : " + NetworkUtils.isNetworkConnected(mContext));
        Log.e(TAG_PET_LIST, "Something went wrong check the logcat logs and figure out!");
        // In case list is empty modify the message
        if (mPetDetails == null) {
            setEmptyTextViewToList(getString(R.string.no_pet_info));
        }
    }

    private void loadInformation() {
        // Load config details
        if (mConfiguration != null) {
            onResponseSuccess(mConfiguration);
        } else {
            new JsonRequestAsyncTask(this).execute(Constants.CONFIG_URL);
        }
        // Load pet information
        if (mPetDetails != null) {
            onResponseSuccess(mPetDetails);
        } else {
            new JsonRequestAsyncTask(this).execute(Constants.PET_LIST_URL);
        }
    }

    private void setEmptyTextViewToList(String textToSet) {
        tvEmptyText.setText(textToSet);
        mPetsListView.setEmptyView(tvEmptyText);
    }
    private AlertDialogFragment isAlertDialogFragmentInBackStack() {
        return (AlertDialogFragment) getFragmentManager().findFragmentByTag(AlertDialogFragment.TAG_ALERT_DIALOG);
    }

    private void displayDialog(String message, boolean isFinishApp) {
        AlertDialogFragment fragment = isAlertDialogFragmentInBackStack();
        if (fragment == null) {
            AlertDialogFragment dialog = AlertDialogFragment.newInstance(message,isFinishApp);
            dialog.setRetainInstance(true);
            dialog.show(getFragmentManager(), AlertDialogFragment.TAG_ALERT_DIALOG);
        }
    }
}

package in.anees.petapp.ui.fragment;

import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.anees.petapp.R;
import in.anees.petapp.data.model.Configuration;
import in.anees.petapp.data.model.Pet;
import in.anees.petapp.viewmodel.PetListViewModel;
import in.anees.petapp.viewmodel.apiresponse.ConfigurationResponse;
import in.anees.petapp.viewmodel.apiresponse.PetListResponse;
import in.anees.petapp.ui.adapter.PetListRecyclerViewAdapter;
import in.anees.petapp.utils.CommonAlert;
import in.anees.petapp.utils.DateUtils;
import in.anees.petapp.utils.NetworkUtils;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 * TODO: Not actively listening to network state change now.
 */
public class PetListFragment extends BaseFragment
        implements View.OnClickListener, PetListRecyclerViewAdapter.OnPetListItemClickListener {

    public static final String TAG = "PetListFragment";

    private Button mButtonChat, mButtonCall;
    private TextView mTvWorkingHours, tvEmptyText;

    private List<Pet> mPetList = new ArrayList<>();
    private Configuration mConfiguration;

    private PetListRecyclerViewAdapter mRecyclerViewAdapter;

    /**
     * Use this factory method to create a new instance of this fragment
     *
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
        mButtonChat = view.findViewById(R.id.buttonChat);
        mButtonChat.setOnClickListener(this);
        mButtonCall = view.findViewById(R.id.buttonCall);
        mButtonCall.setOnClickListener(this);

        mTvWorkingHours = view.findViewById(R.id.tvWorkingHours);

        RecyclerView petsListRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerViewAdapter = new PetListRecyclerViewAdapter(mPetList, this);
        petsListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        petsListRecyclerView.setAdapter(mRecyclerViewAdapter);

        tvEmptyText = view.findViewById(R.id.tvEmptyText);
        setEmptyTextViewToList(getString(R.string.loading));

        PetListViewModel model = ViewModelProviders.of(this).get(PetListViewModel.class);

        // Load if network is there
        if (NetworkUtils.isNetworkConnected(mContext)) {
            Log.i(TAG, "Network connected loading information!");
            // Handle situation if No network Dialog is displayed and then user activate mobile data and try
            if (mConfiguration == null || mPetList == null) {
                AlertDialogFragment fragment = isAlertDialogFragmentInBackStack();
                if (fragment != null) {
                    boolean isOkToExit = fragment.getArguments().getBoolean(AlertDialogFragment.ARG_PARAM2);
                    if (isOkToExit) fragment.dismissAllowingStateLoss();
                }
            }
        } else {
            displayDialog(getString(R.string.no_network), true);
        }

        model.getPets().observe(this, new Observer<PetListResponse>() {
            @Override
            public void onChanged(PetListResponse petListResponse) {
                if (petListResponse == null) {
                    CommonAlert.showToast(mContext, "Pet list returned empty");
                } else if (petListResponse.getPetList() != null) {
                    setPetListValuesSuccess(petListResponse.getPetList());
                } else if (petListResponse.getMessage() != null) {
                    setErrorWhileFetchingPetList(petListResponse.getMessage());
                } else {
                    Log.e(TAG, "onChanged: FetchPetList normally it was not suppose to happen");
                }
            }
        });

        model.getConfiguration().observe(this, new Observer<ConfigurationResponse>() {
            @Override
            public void onChanged(ConfigurationResponse configurationResponse) {
                if (configurationResponse == null) {
                    CommonAlert.showToast(mContext, "Configuration returned empty");
                } else if (configurationResponse.getConfiguration() != null) {
                    setSuccessConfiguration(configurationResponse.getConfiguration());
                } else if (configurationResponse.getMessage() != null) {
                    setErrorFetchingConfiguration(configurationResponse.getMessage());
                } else {
                    Log.e(TAG, "onChanged: configuration normally it was not suppose to happen");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonChat:
            case R.id.buttonCall:
                if (mConfiguration != null) {
                    displayAlertDialogWithMessage(DateUtils.isThisTheRightTime(new Date(), mConfiguration));
                }
                break;
        }
    }


    private void setEmptyTextViewToList(String textToSet) {
        tvEmptyText.setVisibility(View.VISIBLE);
        tvEmptyText.setText(textToSet);
    }

    private void displayAlertDialogWithMessage(boolean isThisTheRightTime) {
        displayDialog((isThisTheRightTime) ?
                getString(R.string.within_work_hours) : getString(R.string.outside_work_hours), false);
    }

    private void setPetListValuesSuccess(final List<Pet> petList) {
        mPetList = petList;
        tvEmptyText.setVisibility(View.GONE);
        mRecyclerViewAdapter.setPetListToRecyclerView(mPetList);
    }

    private void setErrorWhileFetchingPetList(String errorMessage) {
        // TODO : Retry by checking the root cause of failure
        Log.e(TAG, "setErrorWhileFetchingPetList: " + errorMessage);
        setEmptyTextViewToList(getString(R.string.no_pet_info));
    }

    private void setErrorFetchingConfiguration(final String errorMessage) {
        // TODO : Retry by checking the root cause of failure
        Log.e(TAG, "setErrorFetchingConfiguration: " + errorMessage);
        CommonAlert.showToast(mContext, errorMessage);
    }

    private void setSuccessConfiguration(final Configuration configuration) {
        mConfiguration = configuration;
        mButtonCall.setVisibility(mConfiguration.isIsCallEnabled() ? View.VISIBLE : View.GONE);
        mButtonChat.setVisibility(mConfiguration.isIsChatEnabled() ? View.VISIBLE : View.GONE);
        String workingHours = mConfiguration.getWorkHours();
        if (!workingHours.isEmpty()) {
            mTvWorkingHours.setVisibility(View.VISIBLE);
            mTvWorkingHours.setText(workingHours);
        }
    }

    @Override
    public void onPetClick(final int position) {
        Log.d(TAG, "onPetClick: " + mPetList.get(position).getTitle());
        if (!NetworkUtils.isNetworkConnected(mContext)) {
            displayDialog(getString(R.string.no_network), false);
            return;
        }
        PetDetailsFragment petDetailsFragment = PetDetailsFragment
                .newInstance(mPetList.get(position).getTitle(), mPetList.get(position).getContentUrl());
        mListener.addFragment(petDetailsFragment, false, PetDetailsFragment.TAG_PET_DETAILS);
    }
}

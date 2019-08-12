package in.anees.petapp.ui.fragment;

import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.anees.petapp.R;
import in.anees.petapp.data.network.PetAppNetworkService;
import in.anees.petapp.presenter.PetListContract;
import in.anees.petapp.presenter.PetListPresenter;
import in.anees.petapp.data.model.Configuration;
import in.anees.petapp.data.model.Pet;
import in.anees.petapp.ui.adapter.PetListRecyclerViewAdapter;
import in.anees.petapp.utils.CommonAlert;
import in.anees.petapp.utils.NetworkUtils;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 * TODO: Not actively listening to network state change now.
 */
public class PetListFragment extends BaseFragment
        implements View.OnClickListener, PetListRecyclerViewAdapter.OnPetListItemClickListener,
        PetListContract.PetListMvpView {

    public static final String TAG = "PetListFragment";

    private Button mButtonChat, mButtonCall;
    private TextView mTvWorkingHours, tvEmptyText;

    private List<Pet> mPetList = new ArrayList<>();
    private Configuration mConfiguration;

    private PetListPresenter mPetListPresenter;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPetListPresenter = new PetListPresenter(this, new PetAppNetworkService(mContext.getApplicationContext()));
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

        // Load if network is there
        if (NetworkUtils.isNetworkConnected(mContext)) {
            Log.i(TAG, "Network connected loading information!");
            // Handle situation if No network Dialog is displayed and then user activate mobile data and try
            if (mConfiguration == null || mPetList == null) {
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
            if (mConfiguration != null) {
                setSuccessConfiguration(mConfiguration);
            }
            if (mPetList != null) {
                setPetListValuesSuccess(mPetList);
            }

            // If both are not null then it's okay not to show "No internet dialog"
            if (mConfiguration != null && mPetList != null) {
                return;
            }
        }
        displayDialog(getString(R.string.no_network), true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonChat:
            case R.id.buttonCall:
                if (mConfiguration != null) {
                    mPetListPresenter.handleCallOrChatButtonClick(mConfiguration);
                }
                break;
        }
    }

    private void loadInformation() {
        // Load config details
        if (mConfiguration != null) {
            setSuccessConfiguration(mConfiguration);
        } else {
            mPetListPresenter.handleFetchConfiguration();
        }
        // Load pet information
        if (mPetList != null && mPetList.size() > 0) {
            setPetListValuesSuccess(mPetList);
        } else {
            mPetListPresenter.handleFetchPetList();
        }
    }

    private void setEmptyTextViewToList(String textToSet) {
        tvEmptyText.setVisibility(View.VISIBLE);
        tvEmptyText.setText(textToSet);
    }

    @Override
    public void displayAlertDialogWithMessage(boolean isThisTheRightTime, boolean isFinishOkToFinishApp) {
        displayDialog((isThisTheRightTime) ?
                getString(R.string.within_work_hours) : getString(R.string.outside_work_hours), isFinishOkToFinishApp);
    }

    @Override
    public void setPetListValuesSuccess(final List<Pet> petList) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPetList = petList;
                tvEmptyText.setVisibility(View.GONE);
                mRecyclerViewAdapter.setPetListToRecyclerView(mPetList);
            }
        });
    }

    @Override
    public void setErrorWhileFetchingPetList(String errorMessage) {
        // TODO : Retry by checking the root cause of failure
        Log.e(TAG, "setErrorWhileFetchingPetList: " + errorMessage);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                setEmptyTextViewToList(getString(R.string.no_pet_info));
            }
        });
    }

    @Override
    public void setErrorFetchingConfiguration(final String errorMessage) {
        // TODO : Retry by checking the root cause of failure
        Log.e(TAG, "setErrorFetchingConfiguration: " + errorMessage);
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                CommonAlert.showToast(mContext, errorMessage);
            }
        });
    }

    @Override
    public void setSuccessConfiguration(final Configuration configuration) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mConfiguration = configuration;
                mButtonCall.setVisibility(mConfiguration.isIsCallEnabled() ? View.VISIBLE : View.GONE);
                mButtonChat.setVisibility(mConfiguration.isIsChatEnabled() ? View.VISIBLE : View.GONE);
                String workingHours = mConfiguration.getWorkHours();
                if (!workingHours.isEmpty()) {
                    mTvWorkingHours.setVisibility(View.VISIBLE);
                    mTvWorkingHours.setText(workingHours);
                }
            }
        });
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

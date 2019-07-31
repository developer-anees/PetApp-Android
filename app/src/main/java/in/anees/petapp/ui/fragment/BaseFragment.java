package in.anees.petapp.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import in.anees.petapp.MainActivity;
import in.anees.petapp.ui.listeners.FragmentListener;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public class BaseFragment extends Fragment {

    protected MainActivity mContext;
    protected FragmentListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = (MainActivity) context;
        mListener = (FragmentListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
        mListener = null;
    }
}

package in.anees.petapp.ui.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import in.anees.petapp.R;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public class AlertDialogFragment extends DialogFragment {

    public static final String TAG_ALERT_DIALOG = "AlertDialogFragment";

    private static final String ARG_PARAM1 = "message";
    protected static final String ARG_PARAM2 = "isFinishApp";

    public static AlertDialogFragment newInstance(String message, boolean isFinishApp) {
        AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, message);
        args.putBoolean(ARG_PARAM2, isFinishApp);
        alertDialogFragment.setArguments(args);
        return alertDialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setCancelable(false);
        setRetainInstance(true);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String message = getArguments() != null ? getArguments().getString(ARG_PARAM1) : "";
        final boolean isExitActivity = getArguments() != null && getArguments().getBoolean(ARG_PARAM2);

        return new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        if (isExitActivity) {
                            getActivity().finish();
                        }
                        dialog.dismiss();
                    }
                }).create();
    }

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setDismissMessage(null);
        super.onDestroyView();
    }
}

package in.anees.petapp.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

/**
 * Created by Anees Thyrantakath on 2/16/19.
 */
public class CommonAlert {

    private CommonAlert() {
        // This utility class is not publicly instantiable
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}

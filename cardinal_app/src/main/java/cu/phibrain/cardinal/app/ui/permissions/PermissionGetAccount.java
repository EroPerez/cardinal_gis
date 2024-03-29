package cu.phibrain.cardinal.app.ui.permissions;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;

import cu.phibrain.cardinal.app.R;
import eu.geopaparazzi.library.permissions.AChainedPermissionHelper;

public class PermissionGetAccount extends AChainedPermissionHelper {

    protected boolean canAskPermission = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;

    public static int GET_ACCOUNTS_PERMISSION_REQUESTCODE = 88;

    /**
     * Get a description of this permission helper.
     *
     * @return a string that can be used in dialogs.
     */
    @Override
    public String getDescription() {
        return "Get Accounts";
    }

    /**
     * Checks if the permission is granted.
     *
     * @param context the context to use.
     * @return true, if permission is granted.
     */
    @Override
    public boolean hasPermission(Context context) {
        if (canAskPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return context.checkSelfPermission(
                        Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED;
            }
        }
        return true;
    }

    /**
     * Request the permission.
     *
     * @param activity the asking activity.
     */
    @Override
    public void requestPermission(Activity activity) {
        if (canAskPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (activity.checkSelfPermission(
                        Manifest.permission.GET_ACCOUNTS) !=
                        PackageManager.PERMISSION_GRANTED) {

                    if (activity.shouldShowRequestPermissionRationale(
                            Manifest.permission.GET_ACCOUNTS)) {
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(activity);
                        builder.setMessage(activity.getString(R.string.permissions_needed));
                        builder.setPositiveButton(android.R.string.ok,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        activity.requestPermissions(new String[]{
                                                        Manifest.permission.GET_ACCOUNTS},
                                                GET_ACCOUNTS_PERMISSION_REQUESTCODE);
                                    }
                                }
                        );
                        // display the dialog
                        builder.create().show();
                    } else {
                        // request permission
                        activity.requestPermissions(
                                new String[]{Manifest.permission.GET_ACCOUNTS},
                                GET_ACCOUNTS_PERMISSION_REQUESTCODE);
                    }
                }
            }
        }
    }

    /**
     * Checks if the permission has finally been granted.
     * <p>
     * <p>To be checked in the <code>onRequestPermissionsResult</code> method once the request comes back.</p>
     *
     * @param requestCode  the requestcode used.
     * @param grantResults the results array.
     * @return true if permission has been granted.
     */
    @Override
    public boolean hasGainedPermission(int requestCode, int[] grantResults) {
        return requestCode == GET_ACCOUNTS_PERMISSION_REQUESTCODE &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}

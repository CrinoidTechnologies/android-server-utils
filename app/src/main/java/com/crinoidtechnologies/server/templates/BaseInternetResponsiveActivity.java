package com.crinoidtechnologies.server.templates;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;

import com.crinoidtechnologies.general.template.activities.BasePermissionActivity;
import com.crinoidtechnologies.general.template.dialogs.DialogButtonListener;
import com.crinoidtechnologies.general.template.dialogs.DialogPositiveButtonListener;
import com.crinoidtechnologies.general.template.dialogs.SampleDialogs;
import com.crinoidtechnologies.general.utils.ConnectivityUtils;
import com.crinoidtechnologies.general.utils.NetworkHelper;
import com.crinoidtechnologies.general.utils.ProgressDialogUtils;
import com.curlymustachestudios.serverutils.R;

/**
 * Created by ${Vivek} on 5/2/2016 for Avante.Be careful
 */
public abstract class BaseInternetResponsiveActivity extends BasePermissionActivity implements ConnectivityUtils.ConnectivityListener {

    protected int retryCounter = 0;
    protected ProgressDialog dialog;
    protected Dialog noInternetDialog;

    protected void checkForNetwork() {
        if (!NetworkHelper.isConnected(getApplicationContext())) {
            showNoInternetDialog();
        }
    }

    @Override
    protected void onPause() {
        ConnectivityUtils.unregister(this, this);
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityUtils.register(this, this);
        checkForNetwork();
    }

    protected void showNoInternetDialog() {
        if (noInternetDialog == null) {
            noInternetDialog = SampleDialogs.getTryAgainDialog(BaseInternetResponsiveActivity.this, R.string.no_internet_available, false, new DialogPositiveButtonListener() {
                @Override
                public void onPositiveButtonClick(DialogInterface dialog, int which) {
                    if (NetworkHelper.isConnected(getApplicationContext())) {
                        dialog.dismiss();
                    }
                }
            });

            noInternetDialog.setCanceledOnTouchOutside(false);
            noInternetDialog.setCancelable(false);
            noInternetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    findViewById(android.R.id.content).postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkForNetwork();
                        }
                    }, 100);

                }
            });
        }
        if (!noInternetDialog.isShowing()) {
            noInternetDialog.show();
        }
    }

    @Override
    public void onConnectionEstablished() {
        retryCounter = 0;
        if (noInternetDialog != null && noInternetDialog.isShowing()) {
            noInternetDialog.hide();
        }
    }

    @Override
    public void onConnectionLost() {
        showNoInternetDialog();
    }

    protected void showLoadingBar(String message) {
        if (dialog == null) {
            dialog = ProgressDialogUtils.getProgressDialog(this, message);
        }
        dialog.setMessage(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.dismiss();
        dialog.show();
    }

    protected void hideLoadingBar() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    protected void showTryAgainPopUp(DialogButtonListener listener, int message) {
        Dialog dialog = SampleDialogs.getTryAgainDialog(this, message, true, listener);
        dialog.show();
    }

}

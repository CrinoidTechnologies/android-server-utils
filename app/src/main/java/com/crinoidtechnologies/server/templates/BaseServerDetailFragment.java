package com.crinoidtechnologies.server.templates;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crinoidtechnologies.general.models.BaseModel;
import com.crinoidtechnologies.general.template.dialogs.DialogPositiveButtonListener;
import com.crinoidtechnologies.general.template.dialogs.SampleDialogs;
import com.crinoidtechnologies.general.template.fragments.BaseNetworkFragment;
import com.curlymustachestudios.serverutils.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ${Vivek} on 5/4/2016 for Avante.Be careful
 */
public abstract class BaseServerDetailFragment<T extends BaseModel> extends BaseNetworkFragment {

    protected T data;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setInfo();
        //   fetchItem();
    }

    protected abstract void setInfo();

    protected void fetchItem() {

        startServerRequest(getFetchCall(), getString(R.string.fetching));
    }

    protected abstract Call<ResponseBody> getFetchCall();

    private void startServerRequest(Call<ResponseBody> call, String message) {
        showLoadingBar(message);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideLoadingBar();
                if (response.isSuccessful()) {
                    onInfoFetch(response.body());
                } else {

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideLoadingBar();
                showTryAgainPopUp(call, R.string.network_issue);
            }
        });
    }

    private void showTryAgainPopUp(final Call<ResponseBody> call, int messageId) {
        Dialog dialog = SampleDialogs.getTryAgainDialog(getContext(), messageId, true, new DialogPositiveButtonListener() {
            @Override
            public void onPositiveButtonClick(DialogInterface dialog, int which) {
                startServerRequest(call, "");
            }
        });

        dialog.show();
    }

    protected void endActivity() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    protected abstract void onInfoFetch(Object data);

}

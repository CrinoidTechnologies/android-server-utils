package com.crinoidtechnologies.server.templates;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.crinoidtechnologies.general.models.BaseModel;
import com.crinoidtechnologies.general.template.dialogs.DialogButtonListener;
import com.crinoidtechnologies.general.template.dialogs.SampleDialogs;
import com.crinoidtechnologies.general.template.fragments.BaseEditFragment;
import com.crinoidtechnologies.server.models.ServerRequest;
import com.crinoidtechnologies.server.models.ServerRequestCallback;
import com.curlymustachestudios.serverutils.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ${Vivek} on 5/4/2016 for Avante.Be careful
 */
public abstract class BaseServerEditFragment<T extends BaseModel> extends BaseEditFragment<T> implements ServerRequestCallback {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            setDataForBundle(bundle);
        }

        if (data == null) {
            setData();
        }

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void addItem() {
        startServerRequest(getAddItemCall(), getString(R.string.saving));
    }

    @Override
    protected void updateItem() {
        startServerRequest(getUpdateItemCall(), getString(R.string.updating));
    }

    protected abstract Call<ResponseBody> getUpdateItemCall();

    protected abstract Call<ResponseBody> getAddItemCall();

    protected abstract void setDataForBundle(Bundle bundle);

    protected abstract void addParameterToRequest(ServerRequest serverRequest);

    protected abstract String getDataForSaving();

    private void startServerRequest(Call<ResponseBody> call, String message) {
        showLoadingBar(message);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public void deleteItem() {
        //startServerRequest(addServerRequest, getString(R.string.deleting));
    }

    protected void showTryAgainPopUp(final Call<ResponseBody> request, int messageId) {
        Dialog dialog = SampleDialogs.getTryAgainDialog(getContext(), messageId, true, new DialogButtonListener() {
            @Override
            public void onPositiveButtonClick(DialogInterface dialog, int which) {
                startServerRequest(request, "");
            }

            @Override
            public void onNegativeButtonClick(DialogInterface dialog, int which) {

            }

            @Override
            public void onNeutralButtonClick(DialogInterface dialog, int which) {
            }
        });

        dialog.show();
    }

//    @Override
//    public void onSuccess(ServerRequest request, Object data) {
//        Log.d("TAG", "onSuccess: " + data);
//        hideLoadingBar();
//        if (request.getMethod() == ServerRequestMethod.Delete.value) {
//            onSuccessFullyDelete(request, data);
//        } else {
//            onSuccessfulUpdate(request, data);
//        }
//    }
//
//    @Override
//    public void onFailure(ServerRequest request, Error error) {
//        error.printStackTrace();
//        hideLoadingBar();
//        showTryAgainPopUp(request, R.string.network_issue);
//    }

    protected abstract void onSuccessfulUpdate(Response<ResponseBody> data, Object o);

    protected abstract void onSuccessFullyDelete(Response<ResponseBody> request, Object data);

}

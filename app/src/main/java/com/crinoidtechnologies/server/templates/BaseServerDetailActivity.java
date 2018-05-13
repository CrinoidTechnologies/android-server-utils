package com.crinoidtechnologies.server.templates;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.crinoidtechnologies.general.models.BaseModel;
import com.crinoidtechnologies.server.models.ServerRequestCallback;

/**
 * Created by ${Vivek} on 5/4/2016 for Avante.Be careful
 */
public abstract class BaseServerDetailActivity<T extends BaseModel> extends BaseBackButtonActivity implements ServerRequestCallback {
    public static String EXTRA_KEY_OBJ = "obj";

    protected T data;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setInfo(getIntent().getExtras());
        super.onCreate(savedInstanceState);

    }

    protected abstract void setInfo(Bundle extras);

}

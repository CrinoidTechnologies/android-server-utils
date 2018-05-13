package com.crinoidtechnologies.server.templates;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.crinoidtechnologies.general.models.BaseModel;
import com.crinoidtechnologies.general.template.fragments.BaseEditFragment;
import com.curlymustachestudios.serverutils.R;

import java.util.List;

/**
 * Created by ${Vivek} on 5/4/2016 for Avante.Be careful
 */
public abstract class BaseServerEditActivity<T extends BaseModel> extends BaseInternetResponsiveActivity {

    public static final String EXTRA_ID_KEY = "id";
    protected String mainTitle = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContentView();
        //   fetchItem();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent().getExtras() != null) {
            setTitle(getString(R.string.edit) + " " + mainTitle);
            return;
        }
        setTitle(getString(R.string.add) + " " + mainTitle);
    }

    protected abstract void initContentView();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onSaceButtonClick() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();

        for (Fragment fragment :
                fragments) {
            if (fragment instanceof BaseEditFragment) {
                ((BaseEditFragment) fragment).onSaveButtonClick();
            }
        }
    }

}

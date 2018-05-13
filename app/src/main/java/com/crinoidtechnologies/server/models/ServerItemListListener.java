package com.crinoidtechnologies.server.models;

/**
 * Created by ${Vivek} on 5/1/2016 for Avante.Be careful
 */
public interface ServerItemListListener {
    void onListUpdate();

    void onFailure(BaseApiError error);
}

package com.crinoidtechnologies.server.templates;

import com.crinoidtechnologies.server.utils.ErrorUtils;
import com.crinoidtechnologies.server.models.ServerItemListListener;
import com.crinoidtechnologies.server.models.BaseApiError;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ${Vivek} on 5/1/2016 for Avante.Be careful
 */
public abstract class ServerItemListManager<T> {
    public static int START_PAGE = 1;

    protected List<T> items;
    protected boolean isAllItemFetched = false;
    protected List<WeakReference<ServerItemListListener>> listeners;
    protected int itemsToBeFetched = 0;
    protected int start = START_PAGE;
    protected Call<List<T>> fetchItemsCall;

    public ServerItemListManager() {
        items = new ArrayList<>();
        listeners = new ArrayList<>(2);
    }

    public void fetchListFromStart(int number) {
        // cancel any ongoing call...
        if (fetchItemsCall!= null && !fetchItemsCall.isExecuted()) {
            fetchItemsCall.cancel();
        }
        itemsToBeFetched = number;
        isAllItemFetched = false;
        start = START_PAGE;
        startFetching();
    }

    protected void startFetching() {
        fetchItemsCall = getFetchItemsCall();

        if (fetchItemsCall == null) {
            return;
        }

        fetchItemsCall.enqueue(new Callback<List<T>>() {
            @Override
            public void onResponse(final Call<List<T>> call, Response<List<T>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    if (start == 1) {
                        items.clear();
                    }
                    if (response.body().size() == 0) {
                        isAllItemFetched = true;
                    } else {
                        start += 1;
                        addItemsToList(response.body());
                    }

                    for (WeakReference<ServerItemListListener> listener : listeners) {
                        ServerItemListListener listener1 = listener.get();
                        if (listener1 != null) {
                            listener1.onListUpdate();
                        }
                    }
                } else {

                    BaseApiError error = ErrorUtils.parseError(response);

                    for (WeakReference<ServerItemListListener> listener : listeners) {
                        ServerItemListListener listener1 = listener.get();
                        if (listener1 != null) {
                            listener1.onFailure(error);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<T>> call, Throwable t) {
                for (WeakReference<ServerItemListListener> listener : listeners) {
                    ServerItemListListener listener1 = listener.get();
                    if (listener1 != null) {
                        listener1.onListUpdate();
                    }
                }
            }
        });
    }

    public void fetchMoreItems() {
        if (isFetching() || isAllItemFetched) {
            return;
        }
        startFetching();
    }

    protected void addItemsToList(List<T> newItems) {
        T tempItem;
        for (int i = 0; i < newItems.size(); i++) {
            tempItem = newItems.get(i);
            if (!items.contains(tempItem)) {
                items.add(tempItem);
            }
        }
    }

    public void addListListener(ServerItemListListener listListener) {
        if (!listeners.contains(new WeakReference<>(listListener))) {
            listeners.add(new WeakReference<>(listListener));
        }
    }

    public void removeListListener(ServerItemListListener listListener) {
        if (listeners != null) {
            listeners.remove(new WeakReference<>(listListener));
        }
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = null;
        this.items = items;
    }

    public boolean isAllItemFetched() {
        return isAllItemFetched;
    }

    public void setAllItemFetched(boolean allItemFetched) {
        isAllItemFetched = allItemFetched;
    }

    public boolean isFetching() {
        return fetchItemsCall != null && !fetchItemsCall.isExecuted();
    }

    protected abstract Call<List<T>> getFetchItemsCall();

}

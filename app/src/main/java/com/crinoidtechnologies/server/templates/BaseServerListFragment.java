package com.crinoidtechnologies.server.templates;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.crinoidtechnologies.general.models.BaseModel;
import com.crinoidtechnologies.general.template.adapters.BaseListAdapter;
import com.crinoidtechnologies.general.template.fragments.BaseListFragment;
import com.crinoidtechnologies.general.template.recyclerView.BaseRecyclerViewAdapter;
import com.crinoidtechnologies.general.template.recyclerView.StatesRecyclerViewAdapter;
import com.crinoidtechnologies.server.models.ServerItemListListener;
import com.crinoidtechnologies.server.models.BaseApiError;

import java.util.List;

/**
 * Created by ${Vivek} on 4/30/2016 for Avante. Be careful
 */

public abstract class BaseServerListFragment<T extends BaseModel> extends BaseListFragment<T> implements ServerItemListListener {

    protected ServerItemListManager<T> serverItemListManager;
    protected int nItemsToFetch = 10;
    protected boolean isFetchItemOnResume = false;

    public BaseServerListFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setServerList();
        assert serverItemListManager != null;
        serverItemListManager.addListListener(this);
        if (statesAdapter != null) {
            statesAdapter.setState(StatesRecyclerViewAdapter.STATE_LOADING);
        }
        serverItemListManager.fetchListFromStart(nItemsToFetch);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (serverItemListManager != null) {
            serverItemListManager.removeListListener(this);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        if (statesAdapter != null) {
            if (statesAdapter.getAdapter() instanceof BaseListAdapter) {
            }
            statesAdapter = null;
        }
        statesAdapter = new StatesRecyclerViewAdapter(getAdapter(), loadView, emptyView, errorView);
        recyclerView.setAdapter(statesAdapter);

        if (serverItemListManager.isFetching() && serverItemListManager.getItems().size() == 0) {
            statesAdapter.setState(StatesRecyclerViewAdapter.STATE_LOADING);
        } else {
            statesAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
        }
        addListenerForEndlessScrolling();
    }

    @Override
    protected void refreshItems() {
        fetchItemsFromStart();
    }

    protected abstract RecyclerView.Adapter getAdapter();

    @Nullable
    @Override
    public View getView() {
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void addListenerForEndlessScrolling() {
        if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
            final StaggeredGridLayoutManager linearLayoutManager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int[] lastVisibleItems = linearLayoutManager.findLastVisibleItemPositions(null);

                    int lastVisibleItem = totalItemCount;

                    if (lastVisibleItems.length > 0) {
                        lastVisibleItem = lastVisibleItems[lastVisibleItems.length - 1];
                    }

                    if (orientation == LinearLayoutManager.VERTICAL && dy < 0) {
                        return;
                    } else if (orientation == LinearLayoutManager.HORIZONTAL && dx < 0) {
                        return;
                    }

                    if ((totalItemCount - lastVisibleItem) < 4) {
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                fetchMoreItemsIfAvailable();
                            }
                        });
                    }
                }
            });

        } else {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int totalItemCount = linearLayoutManager.getItemCount();
                    int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                    if (orientation == LinearLayoutManager.VERTICAL && dy < 0) {
                        return;
                    } else if (orientation == LinearLayoutManager.HORIZONTAL && dx < 0) {
                        return;
                    }

                    if ((totalItemCount - lastVisibleItem) < 4) {
                        recyclerView.post(new Runnable() {
                            @Override
                            public void run() {
                                fetchMoreItemsIfAvailable();
                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerView != null) {
            recyclerView.getAdapter().notifyDataSetChanged();

        }
        if (isFetchItemOnResume) {
            fetchItemsFromStart();
        }

    }

    protected abstract void setServerList();

    protected void setListStatus() {
        if (getShowingItems().size() == 0) {
            if (serverItemListManager.isAllItemFetched() || serverItemListManager.getItems().size() > 0) {
                statesAdapter.setState(StatesRecyclerViewAdapter.STATE_EMPTY);
            } else {
                statesAdapter.setState(StatesRecyclerViewAdapter.STATE_ERROR);
            }
        } else {
            statesAdapter.setState(StatesRecyclerViewAdapter.STATE_NORMAL);
        }
    }

    @Override
    public void onListUpdate() {
        setListStatus();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onFailure(BaseApiError error) {

    }

    private boolean loadMoreItems() {
        if (!serverItemListManager.isAllItemFetched()) {
            serverItemListManager.fetchMoreItems();
            if (serverItemListManager.getItems().size() == 0) {
                statesAdapter.setState(StatesRecyclerViewAdapter.STATE_LOADING);
            }
            return false;
        }
        setListStatus();
        return true;
    }

    @Override
    public boolean fetchMoreItemsIfAvailable() {
        return loadMoreItems();
    }

    protected void fetchItemsFromStart() {
        serverItemListManager.fetchListFromStart(nItemsToFetch);
    }

    @Override
    protected List<T> getEntityBasedOnSearchString(String newSearchString) {
        searchedItems.clear();
        for (T i : serverItemListManager.getItems()) {
            if (i.containSearchString(newSearchString)) {
                searchedItems.add(i);
            }
        }
        return searchedItems;
    }

    @Override
    protected void onListItemUpdateBySearch(List<T> list) {
        if (list != null) {
            ((BaseRecyclerViewAdapter) statesAdapter.getAdapter()).updateList(list);
            //statesAdapter.getAdapter().notifyDataSetChanged();
            recyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    statesAdapter.getAdapter().notifyDataSetChanged();
                }
            }, 200);
        }
        recyclerView.getAdapter().notifyDataSetChanged();
    }
}

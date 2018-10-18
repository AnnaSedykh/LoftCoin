package com.annasedykh.loftcoin.screens.main.rate;


import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.annasedykh.loftcoin.App;
import com.annasedykh.loftcoin.R;
import com.annasedykh.loftcoin.data.api.Api;
import com.annasedykh.loftcoin.data.db.Database;
import com.annasedykh.loftcoin.data.db.model.CoinEntity;
import com.annasedykh.loftcoin.data.db.model.CoinEntityMapper;
import com.annasedykh.loftcoin.data.prefs.Prefs;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RateFragment extends Fragment implements RateView {

    private static final String LAYOUT_MANAGER_STATE = "layout_manager_state";

    @BindView(R.id.rate_toolbar)
    Toolbar toolbar;

    @BindView(R.id.rate_content)
    ViewGroup content;

    @BindView(R.id.rate_refresh)
    SwipeRefreshLayout refresh;

    @BindView(R.id.rate_recycler)
    RecyclerView recycler;

    private Unbinder unbinder;

    private RatePresenter presenter;
    private RateAdapter adapter;
    private Parcelable layoutManagerState;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Activity activity = getActivity();
        if (activity == null) {
            return;
        }

        Api api = ((App) activity.getApplication()).getApi();
        Prefs prefs = ((App) activity.getApplication()).getPrefs();
        Database database = ((App) activity.getApplication()).getDatabase();
        CoinEntityMapper mapper = new CoinEntityMapper();

        presenter = new RatePresenterImpl(api, prefs, database, mapper);

        adapter = new RateAdapter(prefs);
        adapter.setHasStableIds(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rate, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        unbinder = ButterKnife.bind(this, view);

        toolbar.setTitle(R.string.rate_screen_title);

        recycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        recycler.setHasFixedSize(true);
        recycler.setAdapter(adapter);

        refresh.setOnRefreshListener(() -> presenter.onRefresh());

        if (savedInstanceState != null) {
            layoutManagerState = savedInstanceState.getParcelable(LAYOUT_MANAGER_STATE);
        }

        presenter.attachView(this);
        presenter.getRate();

    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        unbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        //Save scroll position on rotation
        outState.putParcelable(LAYOUT_MANAGER_STATE, recycler.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setCoins(List<CoinEntity> coins) {
        adapter.setCoins(coins);

        if (layoutManagerState != null) {
            recycler.getLayoutManager().onRestoreInstanceState(layoutManagerState);
            layoutManagerState = null;
        }
    }

    @Override
    public void setRefreshing(boolean refreshing) {
        refresh.setRefreshing(refreshing);
    }

    @Override
    public void showCurrencyDialog() {

    }
}

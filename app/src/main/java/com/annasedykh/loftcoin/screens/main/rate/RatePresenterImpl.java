package com.annasedykh.loftcoin.screens.main.rate;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.annasedykh.loftcoin.data.api.Api;
import com.annasedykh.loftcoin.data.api.model.Coin;
import com.annasedykh.loftcoin.data.api.model.RateResponse;
import com.annasedykh.loftcoin.data.db.Database;
import com.annasedykh.loftcoin.data.db.model.CoinEntity;
import com.annasedykh.loftcoin.data.db.model.CoinEntityMapper;
import com.annasedykh.loftcoin.data.prefs.Prefs;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class RatePresenterImpl implements RatePresenter {

    @Nullable
    private RateView view;

    private Api api;
    private Prefs prefs;
    private Database database;
    private CoinEntityMapper mapper;


    RatePresenterImpl(Api api, Prefs prefs, Database database, CoinEntityMapper mapper) {
        this.api = api;
        this.prefs = prefs;
        this.database = database;
        this.mapper = mapper;
    }

    @Override
    public void attachView(RateView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    //Get data from DB
    @Override
    public void getRate() {
        List<CoinEntity> coins = database.getCoins();
        if (view != null) {
            view.setCoins(coins);
        }
    }

    @Override
    public void onRefresh() {
        loadRate();
    }

    //Load data via API
    private void loadRate() {
        api.ticker("structure", prefs.getFiatCurrency().name()).enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(@NonNull Call<RateResponse> call, @NonNull Response<RateResponse> response) {
                if (response.body() != null) {
                    List<Coin> coins = response.body().data;
                    List<CoinEntity> entities = mapper.mapCoins(coins);
                    database.saveCoins(entities);
                    if (view != null) {
                        view.setCoins(entities);
                    }
                }
                if (view != null) {
                    view.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<RateResponse> call, @NonNull Throwable t) {
                if (view != null) {
                    view.setRefreshing(false);
                }
            }
        });
    }

}

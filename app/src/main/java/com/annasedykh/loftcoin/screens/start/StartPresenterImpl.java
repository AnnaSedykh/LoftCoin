package com.annasedykh.loftcoin.screens.start;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

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

class StartPresenterImpl implements StartPresenter {

    private static final String TAG = "StartPresenterImpl";

    private Api api;
    private Prefs prefs;
    private Database database;
    private CoinEntityMapper mapper;

    @Nullable
    private StartView view;

    StartPresenterImpl(Api api, Prefs prefs, Database database, CoinEntityMapper entityMapper) {
        this.api = api;
        this.prefs = prefs;
        this.database = database;
        this.mapper = entityMapper;
    }

    @Override
    public void attachView(StartView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void loadRate() {
        api.ticker("structure", prefs.getFiatCurrency().name()).enqueue(new Callback<RateResponse>() {
            @Override
            public void onResponse(@NonNull Call<RateResponse> call, @NonNull Response<RateResponse> response) {
                if(response.body() != null){
                    List<Coin> coins = response.body().data;
                    List<CoinEntity> entities = mapper.mapCoins(coins);
                    database.saveCoins(entities);
                }

                if(view != null){
                    view.navigateToMainScreen();
                }
            }

            @Override
            public void onFailure(@NonNull Call<RateResponse> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: load rate error ", t);
            }
        });
    }
}

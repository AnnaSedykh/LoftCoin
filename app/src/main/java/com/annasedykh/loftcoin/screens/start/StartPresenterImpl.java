package com.annasedykh.loftcoin.screens.start;

import android.support.annotation.Nullable;
import android.util.Log;

import com.annasedykh.loftcoin.data.api.Api;
import com.annasedykh.loftcoin.data.api.model.RateResponse;
import com.annasedykh.loftcoin.data.prefs.Prefs;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class StartPresenterImpl implements StartPresenter {

    private static final String TAG = "StartPresenterImpl";

    private Api api;
    private Prefs prefs;

    @Nullable
    private StartView view;

    public StartPresenterImpl(Api api, Prefs prefs) {
        this.api = api;
        this.prefs = prefs;
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
            public void onResponse(Call<RateResponse> call, Response<RateResponse> response) {
                if(view != null){
                    view.navigateToMainScreen();
                }
            }

            @Override
            public void onFailure(Call<RateResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: load rate error ", t);
            }
        });
    }
}

package com.annasedykh.loftcoin.screens.main.rate;

import android.support.annotation.Nullable;
import android.util.Log;

import com.annasedykh.loftcoin.data.api.Api;
import com.annasedykh.loftcoin.data.api.model.Coin;
import com.annasedykh.loftcoin.data.db.Database;
import com.annasedykh.loftcoin.data.db.model.CoinEntity;
import com.annasedykh.loftcoin.data.db.model.CoinEntityMapper;
import com.annasedykh.loftcoin.data.model.Fiat;
import com.annasedykh.loftcoin.data.prefs.Prefs;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

class RatePresenterImpl implements RatePresenter {

    private static final String TAG = "RatePresenterImpl";

    @Nullable
    private RateView view;

    private Api api;
    private Prefs prefs;
    private Database database;
    private CoinEntityMapper mapper;

    private CompositeDisposable disposables = new CompositeDisposable();


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
        disposables.dispose();
        this.view = null;
    }

    //Get data from DB
    @Override
    public void getRate() {
        Disposable disposable = database.getCoins()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(coinEntities -> {
                            if (view != null) {
                                view.setCoins(coinEntities);
                            }
                        },
                        throwable -> Log.e(TAG, "getRate: ", throwable));


        disposables.add(disposable);
    }

    @Override
    public void onRefresh() {
        loadRate(true);
    }

    @Override
    public void onMenuItemCurrencyClick() {
        if (view != null) {
            view.showCurrencyDialog();
        }
    }

    @Override
    public void onFiatCurrencySelected(Fiat currency) {
        prefs.setFiatCurrency(currency);
        loadRate(false);
    }

    //Load data via API
    private void loadRate(boolean fromRefresh) {

        if (!fromRefresh) {
            if (view != null) {
                view.showProgress();
            }
        }

        Disposable disposable = api.ticker("structure", prefs.getFiatCurrency().name())
                .subscribeOn(Schedulers.io())
                .map(rateResponse -> {
                    List<Coin> coins = rateResponse.data;
                    List<CoinEntity> entities = mapper.mapCoins(coins);
                    database.saveCoins(entities);
                    return entities;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        entities -> {
                            if (view != null) {
                                if (fromRefresh) {
                                    view.setRefreshing(false);
                                } else {
                                    view.hideProgress();
                                }
                            }
                        },
                        throwable -> {
                            Log.e(TAG, "loadRate: ", throwable);
                            if (view != null) {
                                if (fromRefresh) {
                                    view.setRefreshing(false);
                                } else {
                                    view.hideProgress();
                                }
                            }
                        });

        disposables.add(disposable);
    }

}

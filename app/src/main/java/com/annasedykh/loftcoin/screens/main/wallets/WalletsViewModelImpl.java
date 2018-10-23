package com.annasedykh.loftcoin.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.annasedykh.loftcoin.App;
import com.annasedykh.loftcoin.data.db.Database;
import com.annasedykh.loftcoin.data.db.model.CoinEntity;
import com.annasedykh.loftcoin.data.db.model.WalletEntity;
import com.annasedykh.loftcoin.utils.SingleLiveEvent;

import java.util.Random;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WalletsViewModelImpl extends WalletsViewModel {

    private SingleLiveEvent<Object> selectCurrency = new SingleLiveEvent<>();

    private Database database;
    private CompositeDisposable disposables = new CompositeDisposable();


    public WalletsViewModelImpl(@NonNull Application application) {
        super(application);

        database = ((App) application).getDatabase();
    }

    @Override
    public void onNewWalletClick() {
        selectCurrency.postValue(new Object());
    }

    @Override
    public LiveData<Object> selectCurrency() {
        return selectCurrency;
    }

    @Override
    public void getWallets() {

    }

    @Override
    public void onCurrencySelected(CoinEntity coin) {
        WalletEntity wallet = randomWallet(coin);

        Disposable disposable = Observable.fromCallable(() -> {
            database.saveWallet(wallet);
            return new Object();
        })
                .subscribeOn(Schedulers.io())
                .subscribe();

        disposables.add(disposable);
    }

    private WalletEntity randomWallet(CoinEntity coin) {
        Random random = new Random();
        return new WalletEntity(UUID.randomUUID().toString(), coin.id, 10 * random.nextDouble());
    }

}

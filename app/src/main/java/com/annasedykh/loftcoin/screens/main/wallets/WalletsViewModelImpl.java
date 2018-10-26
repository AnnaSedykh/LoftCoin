package com.annasedykh.loftcoin.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.annasedykh.loftcoin.App;
import com.annasedykh.loftcoin.data.db.Database;
import com.annasedykh.loftcoin.data.db.model.CoinEntity;
import com.annasedykh.loftcoin.data.db.model.WalletEntity;
import com.annasedykh.loftcoin.data.db.model.WalletModel;
import com.annasedykh.loftcoin.utils.SingleLiveEvent;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WalletsViewModelImpl extends WalletsViewModel {

    private SingleLiveEvent<Object> selectCurrency = new SingleLiveEvent<>();
    private MutableLiveData<Boolean> walletsVisible = new MutableLiveData<>();
    private MutableLiveData<Boolean> newWalletVisible = new MutableLiveData<>();
    private MutableLiveData<List<WalletModel>> walletsItems = new MutableLiveData<>();

    private Database database;
    private CompositeDisposable disposables = new CompositeDisposable();


    public WalletsViewModelImpl(@NonNull Application application) {
        super(application);

        database = ((App) application).getDatabase();
    }

    @Override
    public LiveData<Object> selectCurrency() {
        return selectCurrency;
    }

    @Override
    public LiveData<Boolean> walletsVisible() {
        return walletsVisible;
    }

    @Override
    public LiveData<Boolean> newWalletVisible() {
        return newWalletVisible;
    }

    @Override
    public LiveData<List<WalletModel>> wallets() {
        return walletsItems;
    }

    @Override
    public void onNewWalletClick() {
        selectCurrency.postValue(new Object());
    }

    @Override
    public void getWallets() {

        Disposable disposable = database.getWallets()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(walletModels -> {
                    if (walletModels.isEmpty()) {
                        walletsVisible.setValue(false);
                        newWalletVisible.setValue(true);
                    } else {
                        walletsVisible.setValue(true);
                        newWalletVisible.setValue(false);

                        walletsItems.setValue(walletModels);
                    }

                });

        disposables.add(disposable);
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

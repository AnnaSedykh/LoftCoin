package com.annasedykh.loftcoin.screens.main.wallets;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.annasedykh.loftcoin.App;
import com.annasedykh.loftcoin.data.db.Database;
import com.annasedykh.loftcoin.data.db.model.CoinEntity;
import com.annasedykh.loftcoin.data.db.model.TransactionEntity;
import com.annasedykh.loftcoin.data.db.model.TransactionModel;
import com.annasedykh.loftcoin.data.db.model.WalletEntity;
import com.annasedykh.loftcoin.data.db.model.WalletModel;
import com.annasedykh.loftcoin.utils.SingleLiveEvent;

import java.util.ArrayList;
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
    private MutableLiveData<List<TransactionModel>> transactionsItems = new MutableLiveData<>();

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
    public void onWalletChanged(int position) {
        WalletEntity wallet = walletsItems.getValue().get(position).wallet;
        getTransactions(wallet.walletId);
    }

    @Override
    public LiveData<List<WalletModel>> wallets() {
        return walletsItems;
    }

    @Override
    public LiveData<List<TransactionModel>> transactions() {
        return transactionsItems;
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

                        if(walletsItems.getValue() == null || walletsItems.getValue().isEmpty()){
                            WalletModel walletModel = walletModels.get(0);
                            String walletId = walletModel.wallet.walletId;
                            getTransactions(walletId);
                        }

                        walletsItems.setValue(walletModels);
                    }

                });

        disposables.add(disposable);
    }

    private void getTransactions(String walletId) {
        Disposable disposable = database.getTransactions(walletId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(transactions -> transactionsItems.setValue(transactions));

        disposables.add(disposable);
    }

    @Override
    public void onCurrencySelected(CoinEntity coin) {
        WalletEntity wallet = randomWallet(coin);
        List<TransactionEntity> transactions = randomTransactions(wallet);

        Disposable disposable = Observable.fromCallable(() -> {
            database.saveWallet(wallet);
            database.saveTransaction(transactions);
            return new Object();
        })
                .subscribeOn(Schedulers.io())
                .subscribe();

        disposables.add(disposable);
    }

    private WalletEntity randomWallet(CoinEntity coin) {
        Random random = new Random();
        return new WalletEntity(UUID.randomUUID().toString(), coin.coinId, 10 * random.nextDouble());
    }

    private List<TransactionEntity> randomTransactions(WalletEntity wallet) {
        List<TransactionEntity> transactions = new ArrayList<>();

        for (int i = 0; i < 20; i++) {
            transactions.add(randomTransaction(wallet));
        }

        return transactions;
    }

    private TransactionEntity randomTransaction(WalletEntity wallet) {

        Random rand = new Random();

        long startDate = 1514800800000L;
        long nowDate = System.currentTimeMillis();
        long date = startDate + (long) (rand.nextDouble() * (nowDate - startDate));

        double amount = 2 * rand.nextDouble();
        boolean amountSign = rand.nextBoolean();

        return new TransactionEntity(wallet.walletId, wallet.currencyId, amountSign ? amount : -amount, date);
    }

}

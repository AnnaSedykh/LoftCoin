package com.annasedykh.loftcoin.data.db.realm;

import com.annasedykh.loftcoin.data.db.Database;
import com.annasedykh.loftcoin.data.db.model.CoinEntity;
import com.annasedykh.loftcoin.data.db.model.TransactionEntity;
import com.annasedykh.loftcoin.data.db.model.WalletEntity;

import java.util.List;

import io.reactivex.Flowable;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class DatabaseRealmImpl implements Database {

    private Realm realm;

    @Override
    public void open() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void close() {
        realm.close();
    }

    @Override
    public void saveCoins(List<CoinEntity> coins) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(coins);
        realm.commitTransaction();
    }

    @Override
    public void saveWallet(WalletEntity wallet) {
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(wallet);
        realm.commitTransaction();
    }

    @Override
    public void saveTransaction(List<TransactionEntity> transactions) {
        realm.beginTransaction();
        realm.copyToRealm(transactions);
        realm.commitTransaction();
    }

    @Override
    public Flowable<List<CoinEntity>> getCoins() {

        Flowable<RealmResults<CoinEntity>> resultsFlowable = realm.where(CoinEntity.class)
                .findAll()
                .asFlowable()
                .filter(RealmResults::isLoaded);

        //noinspection unchecked
        return (Flowable) resultsFlowable;
    }

    @Override
    public Flowable<List<WalletEntity>> getWallets() {

        Flowable<RealmResults<WalletEntity>> resultsFlowable = realm.where(WalletEntity.class)
                .findAll()
                .asFlowable()
                .filter(RealmResults::isLoaded);

        //noinspection unchecked
        return (Flowable) resultsFlowable;
    }

    @Override
    public Flowable<List<TransactionEntity>> getTransactions(String walletId) {

        Flowable<RealmResults<TransactionEntity>> resultsFlowable = realm.where(TransactionEntity.class)
                .equalTo("walletId", walletId)
                .findAll()
                .sort("date", Sort.DESCENDING)
                .asFlowable()
                .filter(RealmResults::isLoaded);

        //noinspection unchecked
        return (Flowable) resultsFlowable;
    }

    @Override
    public CoinEntity getCoin(String symbol) {
        return realm.where(CoinEntity.class).equalTo("symbol", symbol).findFirst();
    }
}

package com.annasedykh.loftcoin.data.db.room;

import com.annasedykh.loftcoin.data.db.Database;
import com.annasedykh.loftcoin.data.db.model.CoinEntity;
import com.annasedykh.loftcoin.data.db.model.WalletEntity;
import com.annasedykh.loftcoin.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Flowable;

public class DatabaseRoomImpl implements Database {

    private AppDatabase database;

    public DatabaseRoomImpl(AppDatabase database) {
        this.database = database;
    }

    @Override
    public void saveCoins(List<CoinEntity> coins) {
        database.coinDao().saveCoins(coins);
    }

    @Override
    public void saveWallet(WalletEntity wallet) {
        database.walletDao().saveWallet(wallet);
    }

    @Override
    public Flowable<List<CoinEntity>> getCoins() {
        return database.coinDao().getCoins();
    }

    @Override
    public Flowable<List<WalletModel>> getWallets() {
        return database.walletDao().getWallets();
    }

    @Override
    public CoinEntity getCoin(String symbol) {
        return database.coinDao().getCoin(symbol);
    }
}

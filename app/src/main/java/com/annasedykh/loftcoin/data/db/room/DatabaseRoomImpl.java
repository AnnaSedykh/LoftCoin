package com.annasedykh.loftcoin.data.db.room;

import com.annasedykh.loftcoin.data.db.Database;
import com.annasedykh.loftcoin.data.db.model.CoinEntity;

import java.util.List;

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
    public List<CoinEntity> getCoins() {
        return database.coinDao().getCoins();
    }
}

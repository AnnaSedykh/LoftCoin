package com.annasedykh.loftcoin.data.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.annasedykh.loftcoin.data.db.model.CoinEntity;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface CoinDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveCoins(List<CoinEntity> coins);

    @Query("SELECT * FROM Coin")
    Flowable<List<CoinEntity>> getCoins();

    @Query("SELECT * FROM Coin WHERE symbol = :symbol")
    CoinEntity getCoin(String symbol);
}

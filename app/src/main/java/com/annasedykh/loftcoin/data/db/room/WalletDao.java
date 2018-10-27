package com.annasedykh.loftcoin.data.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.annasedykh.loftcoin.data.db.model.WalletEntity;
import com.annasedykh.loftcoin.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveWallet(WalletEntity wallet);

    @Query("SELECT w.*, c.* FROM Wallet w, Coin c WHERE w.currencyId = c.id")
    Flowable<List<WalletModel>> getWallets();
}

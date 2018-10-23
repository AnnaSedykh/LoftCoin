package com.annasedykh.loftcoin.data.db.room;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

import com.annasedykh.loftcoin.data.db.model.WalletEntity;

@Dao
public interface WalletDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveWallet(WalletEntity wallet);
}

package com.annasedykh.loftcoin.data.db.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "Wallet")
public class WalletEntity {

    @PrimaryKey
    @NonNull
    public String walletId;

    public int currencyId;

    public double amount;

    public WalletEntity(String walletId, int currencyId, double amount) {
        this.walletId = walletId;
        this.currencyId = currencyId;
        this.amount = amount;
    }
}

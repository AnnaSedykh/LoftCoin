package com.annasedykh.loftcoin.data.db.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class WalletEntity extends RealmObject {

    @PrimaryKey
    public String walletId;

    public double amount;

    public CoinEntity coin;

    public WalletEntity() {
    }

    public WalletEntity(String walletId, double amount, CoinEntity coin) {
        this.walletId = walletId;
        this.amount = amount;
        this.coin = coin;
    }
}

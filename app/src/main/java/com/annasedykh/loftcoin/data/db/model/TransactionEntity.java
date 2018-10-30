package com.annasedykh.loftcoin.data.db.model;

import io.realm.RealmObject;

public class TransactionEntity extends RealmObject {

    public int transactionId;

    public String walletId;

    public double amount;

    public long date;

    public CoinEntity coin;

    public TransactionEntity() {
    }

    public TransactionEntity(String walletId, double amount, long date, CoinEntity coin) {
        this.walletId = walletId;
        this.amount = amount;
        this.date = date;
        this.coin = coin;
    }
}

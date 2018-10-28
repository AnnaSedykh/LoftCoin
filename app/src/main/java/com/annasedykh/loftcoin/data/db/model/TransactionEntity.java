package com.annasedykh.loftcoin.data.db.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "Transaction")
public class TransactionEntity {

    @PrimaryKey(autoGenerate = true)
    public int transactionId;

    public String walletId;

    public int currencyId;

    public double amount;

    public long date;

    public TransactionEntity(String walletId, int currencyId, double amount, long date) {
        this.walletId = walletId;
        this.currencyId = currencyId;
        this.amount = amount;
        this.date = date;
    }
}

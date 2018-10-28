package com.annasedykh.loftcoin.data.db.model;

import android.arch.persistence.room.Embedded;

public class TransactionModel {

    @Embedded
    public TransactionEntity transaction;

    @Embedded
    public CoinEntity coin;
}

package com.annasedykh.loftcoin.data.db.model;

import android.arch.persistence.room.Embedded;

public class WalletModel {

    @Embedded
    public WalletEntity wallet;

    @Embedded
    public CoinEntity coin;
}

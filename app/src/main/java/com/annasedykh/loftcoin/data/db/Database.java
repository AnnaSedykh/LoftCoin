package com.annasedykh.loftcoin.data.db;

import com.annasedykh.loftcoin.data.db.model.CoinEntity;
import com.annasedykh.loftcoin.data.db.model.WalletEntity;
import com.annasedykh.loftcoin.data.db.model.WalletModel;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {

    void saveCoins(List<CoinEntity> coins);

    void saveWallet(WalletEntity wallet);

    Flowable<List<CoinEntity>> getCoins();

    Flowable<List<WalletModel>> getWallets();

    CoinEntity getCoin(String symbol);
}

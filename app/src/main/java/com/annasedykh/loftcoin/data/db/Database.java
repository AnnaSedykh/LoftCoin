package com.annasedykh.loftcoin.data.db;

import com.annasedykh.loftcoin.data.db.model.CoinEntity;
import com.annasedykh.loftcoin.data.db.model.TransactionEntity;
import com.annasedykh.loftcoin.data.db.model.WalletEntity;

import java.util.List;

import io.reactivex.Flowable;

public interface Database {

    void saveCoins(List<CoinEntity> coins);

    void saveWallet(WalletEntity wallet);

    void saveTransaction(List<TransactionEntity> transactions);

    Flowable<List<CoinEntity>> getCoins();

    Flowable<List<WalletEntity>> getWallets();

    Flowable<List<TransactionEntity>> getTransactions(String walletId);

    CoinEntity getCoin(String symbol);

    void open();

    void close();
}

package com.annasedykh.loftcoin.screens.main.rate;

import com.annasedykh.loftcoin.data.db.model.CoinEntity;

import java.util.List;

public interface RateView {

    void setCoins(List<CoinEntity> coins);

    void setRefreshing(boolean refreshing);

    void showCurrencyDialog();
}

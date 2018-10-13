package com.annasedykh.loftcoin.screens.main.rate;

import com.annasedykh.loftcoin.data.api.model.Coin;

import java.util.List;

public interface RateView {

    void setCoins(List<Coin> coins);

    void setRefreshing(boolean refreshing);

    void showCurrencyDialog();
}

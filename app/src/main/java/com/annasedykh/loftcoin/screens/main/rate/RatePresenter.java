package com.annasedykh.loftcoin.screens.main.rate;

import com.annasedykh.loftcoin.data.model.Fiat;

public interface RatePresenter {

    void attachView(RateView view);

    void detachView();

    void getRate();

    void onRefresh();

    void onMenuItemCurrencyClick();

    void onFiatCurrencySelected(Fiat currency);
}

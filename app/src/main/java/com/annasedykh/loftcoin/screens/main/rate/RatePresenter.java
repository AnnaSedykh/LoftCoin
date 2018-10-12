package com.annasedykh.loftcoin.screens.main.rate;

public interface RatePresenter {

    void attachView(RateView view);

    void detachView();

    void getRate();

    void onRefresh();
}

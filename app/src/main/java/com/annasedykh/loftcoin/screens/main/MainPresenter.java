package com.annasedykh.loftcoin.screens.main;

public interface MainPresenter {

    void attachView(MainView view);

    void detachView();

    void onNavigationItemSelected(int itemId);

}

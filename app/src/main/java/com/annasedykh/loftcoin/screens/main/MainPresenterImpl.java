package com.annasedykh.loftcoin.screens.main;

import android.support.annotation.Nullable;

import com.annasedykh.loftcoin.R;

class MainPresenterImpl implements MainPresenter {

    @Nullable
    private MainView view;

    @Override
    public void attachView(MainView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void onNavigationItemSelected(int itemId) {
        if (view != null) {
            switch (itemId) {
                case R.id.menu_item_accounts:
                    view.showWalletsFragment();
                    break;
                case R.id.menu_item_rate:
                    view.showRateFragment();
                    break;
                case R.id.menu_item_converter:
                    view.showConverterFragment();
                    break;
            }
        }
    }
}

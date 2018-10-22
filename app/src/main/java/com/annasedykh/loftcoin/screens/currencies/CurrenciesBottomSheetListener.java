package com.annasedykh.loftcoin.screens.currencies;

import com.annasedykh.loftcoin.data.db.model.CoinEntity;

public interface CurrenciesBottomSheetListener {

    void onCurrencySelected(CoinEntity coin);
}

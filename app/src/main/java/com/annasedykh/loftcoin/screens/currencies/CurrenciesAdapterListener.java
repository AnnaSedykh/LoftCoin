package com.annasedykh.loftcoin.screens.currencies;

import com.annasedykh.loftcoin.data.db.model.CoinEntity;

interface CurrenciesAdapterListener {

    void onCurrencyClick(CoinEntity coin);

}

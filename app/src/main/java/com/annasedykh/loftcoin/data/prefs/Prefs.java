package com.annasedykh.loftcoin.data.prefs;

import com.annasedykh.loftcoin.data.model.Fiat;

public interface Prefs {

    boolean isFirstLaunch();

    void setFirstLaunch(boolean firstLaunch);

    Fiat getFiatCurrency();

    void setFiatCurrency(Fiat currency);
}

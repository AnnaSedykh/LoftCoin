package com.annasedykh.loftcoin;

import android.app.Application;

import com.annasedykh.loftcoin.data.prefs.Prefs;
import com.annasedykh.loftcoin.data.prefs.PrefsImpl;

public class App extends Application {

    private Prefs prefs;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = new PrefsImpl(this);
    }

    public Prefs getPrefs() {
        return prefs;
    }
}

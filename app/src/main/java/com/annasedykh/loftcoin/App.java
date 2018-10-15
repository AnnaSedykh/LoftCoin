package com.annasedykh.loftcoin;

import android.app.Application;

import com.annasedykh.loftcoin.data.api.Api;
import com.annasedykh.loftcoin.data.api.ApiInitializer;
import com.annasedykh.loftcoin.data.db.Database;
import com.annasedykh.loftcoin.data.db.DatabaseInitializer;
import com.annasedykh.loftcoin.data.prefs.Prefs;
import com.annasedykh.loftcoin.data.prefs.PrefsImpl;

public class App extends Application {

    private Api api;
    private Prefs prefs;
    private Database database;

    @Override
    public void onCreate() {
        super.onCreate();

        prefs = new PrefsImpl(this);
        api = new ApiInitializer().init();
        database = new DatabaseInitializer().init(this);
    }

    public Prefs getPrefs() {
        return prefs;
    }

    public Api getApi() {
        return api;
    }

    public Database getDatabase() {
        return database;
    }
}

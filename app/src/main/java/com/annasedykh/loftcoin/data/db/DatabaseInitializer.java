package com.annasedykh.loftcoin.data.db;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class DatabaseInitializer {

    public void init(Context context){
        Realm.init(context);

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .name("loftcoin.realm")
                .build();

        Realm.setDefaultConfiguration(configuration);
    }
}

package com.annasedykh.loftcoin.data.db;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.annasedykh.loftcoin.data.db.room.AppDatabase;
import com.annasedykh.loftcoin.data.db.room.DatabaseRoomImpl;

public class DatabaseInitializer {

    public Database init(Context context){
        AppDatabase appDatabase = Room
                .databaseBuilder(context, AppDatabase.class, "loftcoin.db")
                .build();
        return new DatabaseRoomImpl(appDatabase);
    }
}

package com.example.yourfamouscoach.data.datasources.local.dataBase.quote;

import android.content.Context;

import androidx.room.Room;

public class QuoteLocal {

    private static final String DATABASE_NAME = "quote-db";
    private static QuoteDatabase instance;

    private QuoteLocal() {
    }

    public static QuoteDatabase getDb(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, QuoteDatabase.class, DATABASE_NAME)
                    .createFromAsset("database/" + DATABASE_NAME + ".db")
                    .fallbackToDestructiveMigrationOnDowngrade()
                    .build();
        }
        return instance;
    }
}

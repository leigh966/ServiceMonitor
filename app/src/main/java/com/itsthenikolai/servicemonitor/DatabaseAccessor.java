package com.itsthenikolai.servicemonitor;

import android.content.Context;

import androidx.room.Room;

import com.itsthenikolai.servicemonitor.db.AppDatabase;

public class DatabaseAccessor {
    public static AppDatabase db;
    public static void init(Context ctxt)
    {
        db = Room.databaseBuilder(ctxt,
                        AppDatabase.class, "service-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }
}

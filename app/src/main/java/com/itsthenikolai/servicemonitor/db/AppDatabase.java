package com.itsthenikolai.servicemonitor.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Device.class, Service.class}, version=1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DeviceDao deviceDao();
    public abstract ServiceDao serviceDao();
}

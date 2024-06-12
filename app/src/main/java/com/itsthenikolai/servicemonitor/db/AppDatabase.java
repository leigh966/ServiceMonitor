package com.itsthenikolai.servicemonitor.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Device.class, Service.class, StatusLog.class}, version=2)
@TypeConverters({DateTimeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract DeviceDao deviceDao();
    public abstract ServiceDao serviceDao();

    public abstract StatusLogDao statusLogDao();
}

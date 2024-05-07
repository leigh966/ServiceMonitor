package com.itsthenikolai.servicemonitor;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Device.class}, version=1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DeviceDao deviceDao();
}

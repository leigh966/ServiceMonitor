package com.itsthenikolai.servicemonitor.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.itsthenikolai.servicemonitor.db.Device.Device;
import com.itsthenikolai.servicemonitor.db.Device.DeviceDao;
import com.itsthenikolai.servicemonitor.db.Logs.StatusLog;
import com.itsthenikolai.servicemonitor.db.Logs.StatusLogDao;
import com.itsthenikolai.servicemonitor.db.Service.Service;
import com.itsthenikolai.servicemonitor.db.Service.ServiceDao;

@Database(entities = {Device.class, Service.class, StatusLog.class}, version=3)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DeviceDao deviceDao();
    public abstract ServiceDao serviceDao();

    public abstract StatusLogDao statusLogDao();
}

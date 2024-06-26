package com.itsthenikolai.servicemonitor.db.Device;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface DeviceDao {
    @Query("SELECT * FROM device")
    List<Device> getAll();

    @Insert
    void insertAll(Device... devices);


    @Transaction
    @Query("SELECT * FROM Device")
    public List<DeviceWithServices> getDeviceWithServices();

    @Query("SELECT * FROM Device WHERE uid=:uid")
    public Device get(int uid);
}

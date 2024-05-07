package com.itsthenikolai.servicemonitor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DeviceDao {
    @Query("SELECT * FROM device")
    List<Device> getAll();

    @Insert
    void insertAll(Device... devices);
}

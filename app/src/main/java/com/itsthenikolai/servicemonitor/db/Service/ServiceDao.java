package com.itsthenikolai.servicemonitor.db.Service;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ServiceDao {
    @Insert
    void insertAll(Service... services);

    @Query("SELECT * FROM service")
    List<Service> getAll();

    @Query("SELECT * FROM service WHERE deviceId = :deviceId")
    List<Service> getAllForDevice(int deviceId);

    @Query("DELETE FROM service WHERE uid=:uid")
    void deleteById(int uid);
}

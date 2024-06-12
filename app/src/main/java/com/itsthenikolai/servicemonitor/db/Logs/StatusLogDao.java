package com.itsthenikolai.servicemonitor.db.Logs;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StatusLogDao {

    @Insert
    public void insertAll(StatusLog... logs);

    @Query("SELECT * FROM statuslog ORDER BY datetime DESC")
    public List<StatusLog> getAll();

    @Query("SELECT * FROM statuslog WHERE service_id=:serviceId ORDER BY datetime DESC")
    public List<StatusLog> getByServiceId(int serviceId);
}

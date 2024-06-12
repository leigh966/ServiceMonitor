package com.itsthenikolai.servicemonitor.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface StatusLogDao {

    @Insert
    public void insertAll(StatusLog... logs);

    @Query("SELECT * FROM statuslog")
    public List<StatusLog> getAll();

    @Query("SELECT * FROM statuslog WHERE service_id=:serviceId")
    public List<StatusLog> getByServiceId(int serviceId);
}

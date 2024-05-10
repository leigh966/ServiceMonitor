package com.itsthenikolai.servicemonitor;

import androidx.annotation.NonNull;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ServiceDao {
    @Insert
    void insertAll(Service... services);

    @Query("SELECT * FROM service")
    List<Service> getAll();


}

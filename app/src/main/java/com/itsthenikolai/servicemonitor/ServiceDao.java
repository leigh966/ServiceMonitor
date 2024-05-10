package com.itsthenikolai.servicemonitor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface ServiceDao {
    @Insert
    void insertAll(Service... services);
}

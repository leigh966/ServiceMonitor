package com.itsthenikolai.servicemonitor.db;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.itsthenikolai.servicemonitor.ServiceState;

import java.time.LocalDateTime;

@Entity
public class StatusLog {
    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "code")
    public String code;

    @ColumnInfo(name="status")
    public ServiceState status;

    @ColumnInfo(name="service_id")
    public int serviceId;

    @ColumnInfo(name="datetime")
    public LocalDateTime dateTime;
}

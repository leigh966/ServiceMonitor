package com.itsthenikolai.servicemonitor.db;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.itsthenikolai.servicemonitor.ServiceState;

import java.time.LocalDateTime;

@Entity
public class StatusLog {

    public StatusLog()
    {}


    @RequiresApi(api = Build.VERSION_CODES.O)
    public StatusLog(int code, int serviceId)
    {
        this.code = Integer.toString(code);
        status = code >= 200 && code < 300 ? ServiceState.RUN : ServiceState.FAILED;
        this.serviceId = serviceId;
        dateTime = LocalDateTime.now();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public StatusLog(ServiceState status, int serviceId)
    {
        code = null;
        this.status = status;
        this.serviceId = serviceId;
        dateTime = LocalDateTime.now();
    }


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

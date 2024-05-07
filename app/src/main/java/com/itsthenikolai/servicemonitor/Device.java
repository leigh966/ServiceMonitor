package com.itsthenikolai.servicemonitor;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Device {
    @PrimaryKey
    public int uid;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="ip")
    public String ip;


    public Device(String name, String ip)
    {
        this.name = name;
        this.ip = ip;
    }
}

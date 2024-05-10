package com.itsthenikolai.servicemonitor;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity
public class Service {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="endpoint")
    public String endpoint;

    @ColumnInfo(name="port")
    public int port;

    @ColumnInfo(name="deviceId")
    public int deviceId;

}

package com.itsthenikolai.servicemonitor;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DeviceWithServices {
    @Embedded public Device device;
    @Relation(
            parentColumn = "uid",
            entityColumn = "deviceId"
    )
    public List<Service> services;
}

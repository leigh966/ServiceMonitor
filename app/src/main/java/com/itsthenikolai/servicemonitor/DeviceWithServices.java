package com.itsthenikolai.servicemonitor;

import androidx.annotation.NonNull;
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


    @Override
    @NonNull
    public String toString()
    {
        return String.format("device_name: %s\nservices: %s", device.name, services.toString());
    }

}

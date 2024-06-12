package com.itsthenikolai.servicemonitor.db;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.room.TypeConverter;

import java.time.LocalDateTime;

public class DateTimeConverter {
    @TypeConverter
    public static String datetimeToTimestamp(LocalDateTime ldt)
    {
        return ldt.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @TypeConverter
    public static LocalDateTime timestampToDatetime(String timestamp)
    {
        return LocalDateTime.parse(timestamp);
    }

}

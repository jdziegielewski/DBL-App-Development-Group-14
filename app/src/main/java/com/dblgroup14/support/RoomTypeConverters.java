package com.dblgroup14.support;

import androidx.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Type converter functions for the Room Database library
 */
public class RoomTypeConverters {
    @TypeConverter
    public static Date dateFromLong(Long value) {
        return value == null ? null : new Date(value);
    }
    
    @TypeConverter
    public static Long dateToLong(Date date) {
        return date == null ? null : date.getTime();
    }
    
    @TypeConverter
    public static boolean[] booleanArrFromString(String value) {
        String[] parts = value.split(",");
        boolean[] result = new boolean[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Boolean.valueOf(parts[i]);
        }
        return result;
    }
    
    @TypeConverter
    public static String booleanArrToString(boolean[] array) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) {
                builder.append(',');
            }
            builder.append(array[i]);
        }
        return builder.toString();
    }
    
    @TypeConverter
    public static Map<String, String> mapFromJson(String value) {
        Type listType = new TypeToken<Map<String, String>>() {}.getType();
        return new Gson().fromJson(value, listType);
    }
    
    @TypeConverter
    public static String mapToJson(Map<String, String> map) {
        return new Gson().toJson(map);
    }
}

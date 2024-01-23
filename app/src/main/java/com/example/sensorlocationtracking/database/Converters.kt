package com.example.sensorlocationtracking.database

import androidx.room.TypeConverter
import com.example.sensorlocationtracking.model.LocationData
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

class Converters {

    @TypeConverter
    fun toCategoriesList(list: String): ArrayList<LocationData> {
        val typeToken: Type = object : TypeToken<ArrayList<LocationData>>() {}.type
        return Gson().fromJson(list, typeToken)
    }

    @TypeConverter
    fun fromCategoriesList(list: ArrayList<LocationData>): String {
        return Gson().toJson(list)
    }
}
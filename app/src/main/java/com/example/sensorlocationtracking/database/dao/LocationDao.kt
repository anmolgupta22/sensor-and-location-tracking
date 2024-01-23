package com.example.sensorlocationtracking.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sensorlocationtracking.model.LocationDataList

@Dao
interface LocationDao {

    @Query("Select * from tbl_tracking")
    suspend fun fetchAllLocationTracking(): List<LocationDataList>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(locationDataList: LocationDataList)

    @Query("SELECT * FROM tbl_tracking ORDER BY id DESC LIMIT 1")
    suspend fun fetchMakers() : LocationDataList?

}
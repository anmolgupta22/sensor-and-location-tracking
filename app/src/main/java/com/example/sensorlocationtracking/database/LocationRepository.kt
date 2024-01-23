package com.example.sensorlocationtracking.database

import com.example.sensorlocationtracking.database.dao.LocationDao
import com.example.sensorlocationtracking.model.LocationDataList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class LocationRepository @Inject constructor(private val locationDao: LocationDao) {

    fun insertLocationList(locationDataList: LocationDataList) {
        locationDao.insert(locationDataList)
    }

    suspend fun fetchAllLocationTracking(): List<LocationDataList> {
        return withContext(Dispatchers.IO) {
            locationDao.fetchAllLocationTracking()
        }
    }

    suspend fun fetchMakers(): LocationDataList? {
        return withContext(Dispatchers.IO) {
            locationDao.fetchMakers()
        }
    }
}

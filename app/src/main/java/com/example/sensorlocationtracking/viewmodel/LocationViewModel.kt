package com.example.sensorlocationtracking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sensorlocationtracking.database.LocationRepository
import com.example.sensorlocationtracking.model.LocationDataList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


class LocationViewModel @Inject constructor(private val repository: LocationRepository) :
    ViewModel() {

    fun insertLocationList(list: LocationDataList) {
        repository.insertLocationList(list)
    }

    suspend fun fetchAllLocationTracking(): List<LocationDataList> {
        val job = viewModelScope.async {
            repository.fetchAllLocationTracking()
        }
        return job.await()
    }

    suspend fun fetchMakers(): LocationDataList? {
        val job = viewModelScope.async {
            repository.fetchMakers()
        }
        return job.await()
    }
}
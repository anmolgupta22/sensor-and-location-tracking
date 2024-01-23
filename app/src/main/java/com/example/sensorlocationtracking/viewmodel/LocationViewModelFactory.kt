package com.example.sensorlocationtracking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sensorlocationtracking.database.LocationRepository
import javax.inject.Inject

class LocationViewModelFactory @Inject constructor(private val instance: LocationRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(LocationRepository::class.java).newInstance(instance)
    }
}
package com.example.sensorlocationtracking.di

import com.example.sensorlocationtracking.database.LocationRepository
import com.example.sensorlocationtracking.viewmodel.LocationViewModel
import dagger.Module
import dagger.Provides

@Module
class ViewModelModule {
    @Provides
    fun provideMyViewModel(locationRepository: LocationRepository): LocationViewModel {
        return LocationViewModel(locationRepository)
    }
}

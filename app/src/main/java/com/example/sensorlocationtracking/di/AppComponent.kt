package com.example.sensorlocationtracking.di

import com.example.sensorlocationtracking.fragment.LocationTrackingFragment
import com.example.sensorlocationtracking.fragment.PreviousTrackingFragment
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, ViewModelModule::class, DatabaseModule::class])
@Singleton
interface AppComponent {
    fun inject(activity: LocationTrackingFragment)
    fun inject(activity: PreviousTrackingFragment)
}
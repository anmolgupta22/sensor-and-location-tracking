package com.example.sensorlocationtracking.di

import android.app.Application
import com.example.sensorlocationtracking.MyApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: MyApplication) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application
}

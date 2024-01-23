package com.example.sensorlocationtracking

import android.app.Application
import com.example.sensorlocationtracking.di.AppComponent
import com.example.sensorlocationtracking.di.AppModule
import com.example.sensorlocationtracking.di.DaggerAppComponent


class MyApplication : Application() {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }
}

package com.example.sensorlocationtracking.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import com.example.sensorlocationtracking.model.LocationEvent
import com.example.sensorlocationtracking.R
import com.example.sensorlocationtracking.utils.Constant
import com.example.sensorlocationtracking.SplashScreenActivity
import com.google.android.gms.location.*
import org.greenrobot.eventbus.EventBus

class LocationService : Service(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var gyroscope: Sensor? = null
    private var magnetometer: Sensor? = null

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var locationCallback: LocationCallback? = null
    private var locationRequest: LocationRequest? = null
    private var notificationManager: NotificationManager? = null
    private var location: Location? = null
    private var locationResultData: LocationResult? =null

    override fun onCreate() {
        super.onCreate()

        // Initialize SensorManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // Initialize sensors
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000).setIntervalMillis(500)
                .build()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                // Register SensorListeners
                locationResultData = locationResult
                accelerometer?.let { sensorManager.registerListener(this@LocationService, it, SensorManager.SENSOR_DELAY_NORMAL) }
                gyroscope?.let { sensorManager.registerListener(this@LocationService, it, SensorManager.SENSOR_DELAY_NORMAL) }
                magnetometer?.let { sensorManager.registerListener(this@LocationService, it, SensorManager.SENSOR_DELAY_NORMAL) }
            }
        }
        notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel =
            NotificationChannel(Constant.CHANNEL_ID,
                "locations",
                NotificationManager.IMPORTANCE_DEFAULT)
        notificationChannel.lightColor = Color.BLUE
        notificationChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        notificationManager?.createNotificationChannel(notificationChannel)
    }

    @Suppress("MissingPermission")
    fun createLocationRequest() {
        try {
            fusedLocationProviderClient?.requestLocationUpdates(
                locationRequest!!, locationCallback!!, null
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    private fun removeLocationUpdates() {
        locationCallback?.let {
            fusedLocationProviderClient?.removeLocationUpdates(it)
        }
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
        stopSelf()
    }


    private fun getNotification(): Notification {
        val intent = Intent(applicationContext, SplashScreenActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            2,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val notification = NotificationCompat.Builder(this, Constant.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_location)
            .setContentTitle("Google Map Location Tracking")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
        notification.setChannelId(Constant.CHANNEL_ID)
        return notification.build()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        createLocationRequest()
        startForeground(Constant.NOTIFICATION_ID, getNotification())
        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        removeLocationUpdates()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        // Handle sensor data changes here
        if (event != null) {
            location = locationResultData?.lastLocation
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> {
                    val acceleration = event.values
                    // Handle accelerometer data
                    EventBus.getDefault().post(LocationEvent(
                        altitude = location?.altitude,
                        speed = location?.speed,
                        latitude = location?.latitude,
                        longitude = location?.longitude,
                        acceleration = acceleration
                    ))
                }
                Sensor.TYPE_GYROSCOPE -> {
                    val gyroscopeValues = event.values
                    // Handle gyroscope data
                    EventBus.getDefault().post(LocationEvent(
                        altitude = location?.altitude,
                        speed = location?.speed,
                        latitude = location?.latitude,
                        longitude = location?.longitude,
                        gyroscope = gyroscopeValues
                    ))
                }
                Sensor.TYPE_MAGNETIC_FIELD -> {
                    val magnetometerValues = event.values
                    // Handle magnetometer data
                    EventBus.getDefault().post(LocationEvent(
                        altitude = location?.altitude,
                        speed = location?.speed,
                        latitude = location?.latitude,
                        longitude = location?.longitude,
                        magnetometer = magnetometerValues
                    ))
                }
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        sensorManager.unregisterListener(this)
    }
}
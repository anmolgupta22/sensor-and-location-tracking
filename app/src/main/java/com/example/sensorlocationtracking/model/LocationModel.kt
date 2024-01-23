package com.example.sensorlocationtracking.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.android.gms.maps.model.LatLng

data class LocationEvent(
    var altitude: Double? = null,
    var speed: Float? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var acceleration: FloatArray? = null,
    var gyroscope: FloatArray? = null,
    var magnetometer: FloatArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocationEvent

        if (acceleration != null) {
            if (other.acceleration == null) return false
            if (!acceleration.contentEquals(other.acceleration)) return false
        } else if (other.acceleration != null) return false
        if (gyroscope != null) {
            if (other.gyroscope == null) return false
            if (!gyroscope.contentEquals(other.gyroscope)) return false
        } else if (other.gyroscope != null) return false
        if (magnetometer != null) {
            if (other.magnetometer == null) return false
            if (!magnetometer.contentEquals(other.magnetometer)) return false
        } else if (other.magnetometer != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = acceleration?.contentHashCode() ?: 0
        result = 31 * result + (gyroscope?.contentHashCode() ?: 0)
        result = 31 * result + (magnetometer?.contentHashCode() ?: 0)
        return result
    }
}

@Entity(tableName = "tbl_tracking")
data class LocationDataList(

    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo
    var startMarker: Int = 0,

    @ColumnInfo
    var endMarker: Int = 0,

    @ColumnInfo
    var velocity: Double? = null,

    @ColumnInfo
    var locationData: ArrayList<LocationData> = arrayListOf(),
)

data class LocationData(
    var altitude: Double? = null,
    var speed: Float? = null,
    var velocity: Float? = null,
    var latLng: LatLng? = null,
    var acceleration: FloatArray? = null,
    var gyroscope: FloatArray? = null,
    var magnetometer: FloatArray? = null,
    var timestamp: Long? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocationData

        if (acceleration != null) {
            if (other.acceleration == null) return false
            if (!acceleration.contentEquals(other.acceleration)) return false
        } else if (other.acceleration != null) return false
        if (gyroscope != null) {
            if (other.gyroscope == null) return false
            if (!gyroscope.contentEquals(other.gyroscope)) return false
        } else if (other.gyroscope != null) return false
        if (magnetometer != null) {
            if (other.magnetometer == null) return false
            if (!magnetometer.contentEquals(other.magnetometer)) return false
        } else if (other.magnetometer != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = acceleration?.contentHashCode() ?: 0
        result = 31 * result + (gyroscope?.contentHashCode() ?: 0)
        result = 31 * result + (magnetometer?.contentHashCode() ?: 0)
        return result
    }
}

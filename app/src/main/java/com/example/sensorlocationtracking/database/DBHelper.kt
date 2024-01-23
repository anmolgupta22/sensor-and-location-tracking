package com.example.sensorlocationtracking.database

import android.content.Context
import androidx.room.*
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sensorlocationtracking.model.LocationDataList
import com.example.sensorlocationtracking.database.dao.LocationDao

@Database(
    entities = [LocationDataList::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class DBHelper : RoomDatabase() {

    abstract fun locationDao(): LocationDao

    companion object {

        private var appDataBaseInstance: DBHelper? = null

        @Synchronized
        fun getInstance(context: Context): DBHelper {
            if (appDataBaseInstance == null) {
                appDataBaseInstance = Room.databaseBuilder(
                    context.applicationContext,
                    DBHelper::class.java,
                    "tracking_database"
                )
                    .allowMainThreadQueries()
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }
            return appDataBaseInstance!!
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("CREATE TABLE tbl_location (`id` INT PRIMARY KEY, `startMarker` INT, `endMarker` INT,`velocity` Double, `location_data` TEXT)")
                db.execSQL("INSERT INTO tbl_location (`startMarker`, `endMarker`, `velocity` `location_data`)")
            }
        }
    }
}
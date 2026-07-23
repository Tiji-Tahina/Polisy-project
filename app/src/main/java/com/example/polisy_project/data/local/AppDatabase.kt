package com.example.polisy_project.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.polisy_project.data.local.dao.DriverDao
import com.example.polisy_project.data.local.dao.InfractionDao
import com.example.polisy_project.data.local.dao.SyncQueueDao
import com.example.polisy_project.data.local.dao.VehicleDao
import com.example.polisy_project.data.local.entity.DriverEntity
import com.example.polisy_project.data.local.entity.InfractionEntity
import com.example.polisy_project.data.local.entity.SyncQueueItem
import com.example.polisy_project.data.local.entity.VehicleEntity

@Database(
    entities = [DriverEntity::class, VehicleEntity::class, InfractionEntity::class, SyncQueueItem::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun driverDao(): DriverDao
    abstract fun vehicleDao(): VehicleDao
    abstract fun infractionDao(): InfractionDao
    abstract fun syncQueueDao(): SyncQueueDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "polisy_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

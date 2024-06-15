package dev.ashutoshwahane.matchmate.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProfileEntity::class],
    version = 2,
    exportSchema = true
)
abstract class DatabaseInstance : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
}
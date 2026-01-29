package com.example.gym.data.local

import android.app.Application
import android.content.Context
import androidx.activity.contextaware.ContextAware
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MemberEntity::class, AttendanceEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun memberDao(): MemberDao
    abstract fun attendanceDao(): AttendanceDao

    //static singleton obj made
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(application: Application): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    application.applicationContext,
                    AppDatabase::class.java,
                    "gym_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}
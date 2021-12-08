package com.ikazmin.photodiary.shotDatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Shot::class],version = 6,exportSchema = false)
abstract class ShotDatabase: RoomDatabase(){

    abstract val shotDao: ShotDao

    companion object{
        @Volatile
        private var INSTANCE: ShotDatabase? = null
        fun getInstance(context: Context):ShotDatabase{
            synchronized(this){
                var instance = INSTANCE
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ShotDatabase::class.java,
                        "shot_database")
                        .fallbackToDestructiveMigration().build()
                    INSTANCE = instance

                }
                return instance
            }
        }
    }
}
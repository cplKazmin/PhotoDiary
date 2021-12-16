package com.ikazmin.photodiary.shotDatabase

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ShotDao{

    @Insert
    suspend fun insert(shot:Shot)

    @Update
    suspend fun update(shot: Shot)

    @Query("SELECT * FROM shot_table ORDER BY shotId DESC")
    fun getAllShots(): LiveData<List<Shot>>

    @Query("SELECT * FROM shot_table ORDER BY shotId DESC LIMIT 1")
    suspend fun getLastShot(): Shot?

    @Query("DELETE FROM shot_table WHERE shotId=:id  ")
    suspend fun clear(id: Long)

    @Query("SELECT * FROM shot_table WHERE shotId=:id LIMIT 1")
    fun getShotByKey (id:Long): Shot?

    @Query ("UPDATE shot_table SET short_description =:description WHERE shotId =:id")
    fun updateDescription (id: Long,description:String)

    @Query ("UPDATE shot_table SET image =:image WHERE shotId =:id")
       fun updateImage (id:Long, image:String)

}
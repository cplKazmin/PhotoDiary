package com.ikazmin.photodiary.shotDatabase

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.net.URI

@Entity(tableName = "shot_table")
data class Shot(
    @PrimaryKey(autoGenerate = true)
    var shotId: Long = 0L,

    @ColumnInfo(name ="name")
    val name: String = "noName",

    @ColumnInfo(name = "date_milli")
    val date: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "shutter_speed_int")
    val shutterSpeed: String = "Empty",

    @ColumnInfo(name = "diafragm_int")
    val diafragm: String = "Empty",

    @ColumnInfo(name = "iso_int")
    val iso: String = "Empty",

    @ColumnInfo(name = "short_description")
    val shortDescription: String = "",

    @ColumnInfo (name = "image")
       val imageUri: String = ""
)
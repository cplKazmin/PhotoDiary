package com.ikazmin.photodiary.mainPage

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.ikazmin.photodiary.shotDatabase.ShotDao

class MainPageViewModel(
    database: ShotDao,
    application: Application):AndroidViewModel(application){
        val shots = database.getAllShots()
}
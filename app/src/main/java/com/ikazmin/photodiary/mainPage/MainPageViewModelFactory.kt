package com.ikazmin.photodiary.mainPage

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ikazmin.photodiary.shotDatabase.ShotDao
import java.lang.IllegalArgumentException

class MainPageViewModelFactory (
    private val dataSource: ShotDao,
    private val application: Application
): ViewModelProvider.Factory{
    @Suppress("unchecked_cast")

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainPageViewModel::class.java)){
            return MainPageViewModel(dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
}}

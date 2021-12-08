package com.ikazmin.photodiary.shotdetails

import android.app.Application
import android.app.KeyguardManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ikazmin.photodiary.shotDatabase.ShotDao
import java.lang.IllegalArgumentException

class ShotDetailsViewModelFactory (
    private val dataSource: ShotDao,
    private val application: Application, private val key:String
        ):ViewModelProvider.Factory{
    @Suppress("unchecked cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShotDetailsViewModel::class.java)){
            return ShotDetailsViewModel(dataSource,application,key) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}
package com.ikazmin.photodiary.newFilm

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ikazmin.photodiary.shotDatabase.ShotDao
import java.lang.IllegalArgumentException

class NewShotViewModelFactory (
    private val dataSource: ShotDao,
    private val application: Application):ViewModelProvider.Factory{
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewShotFragmentViewModel::class.java)){
            return NewShotFragmentViewModel(dataSource,application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}

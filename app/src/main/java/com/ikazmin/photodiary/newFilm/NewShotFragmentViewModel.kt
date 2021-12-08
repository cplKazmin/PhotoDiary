package com.ikazmin.photodiary.newFilm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ikazmin.photodiary.shotDatabase.Shot
import com.ikazmin.photodiary.shotDatabase.ShotDao
import kotlinx.coroutines.launch

class NewShotFragmentViewModel(
    private val database: ShotDao,
    application: Application): AndroidViewModel(application) {

    //Для навигации
    private val _navigateToMainPage = MutableLiveData<Boolean?>()
    val navigateToMainPage: LiveData<Boolean?>
        get() = _navigateToMainPage
    fun doneNavigating() {
        _navigateToMainPage.value = null
    }

    // для сохранения данных с фрагмента в бд
    fun onSaveButtonPressed(
        name: String,
        iso: String,
        shutterSpeed: String,
        diafragm: String,
    ) {
        viewModelScope.launch {
            database.insert(
                Shot(
                    diafragm = diafragm,
                    iso = iso,
                    shutterSpeed = shutterSpeed,
                    name = name,
                )
            )
        }
        _navigateToMainPage.value = true
    }

    fun isFilled(name:String,iso: String,shutterSpeed: String,diafragm: String):Boolean{
        return name.isNotEmpty()&&iso.isNotEmpty()&&shutterSpeed.isNotEmpty()&&diafragm.isNotEmpty()
    }

}

package com.ikazmin.photodiary.shotdetails

import android.app.Application
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ikazmin.photodiary.shotDatabase.Shot
import com.ikazmin.photodiary.shotDatabase.ShotDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ShotDetailsViewModel (
    private val database: ShotDao,application: Application, private val key: String
):AndroidViewModel(application){

    private val _navigateToMainPage = MutableLiveData<Boolean?>()
    val navigateToMainPage: LiveData<Boolean?>
        get() = _navigateToMainPage

   fun onDeletePressed(){

        viewModelScope.launch {
            database.clear(key.toLong())
        }
        _navigateToMainPage.value = true
       dialog.isPositive.value = false
    }

   val dialog = DeleteDialog()

    val shot = MutableLiveData<Shot>()
    val description = MutableLiveData<String>()

    fun onDescriptionAdded(){
        viewModelScope.launch {
            updateDescription()

        }
    }

    private suspend fun updateDescription(){
        withContext(Dispatchers.IO){
            database.updateDescription(key.toLong(),description.value!!)
        }
    }

    init {
        initShot()
    }

    private fun initShot(){
        viewModelScope.launch {
            shot.value = getShotFromDatabase()
        }
    }

    private suspend fun getShotFromDatabase(): Shot?{
        return withContext(Dispatchers.IO) {
            return@withContext database.getShotByKey(key.toLong())
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    val MyDateFormat = SimpleDateFormat("dd.MM.yyyy")

    @RequiresApi(Build.VERSION_CODES.N)
    val MyTimeFormat = SimpleDateFormat("h:mm a")

    }


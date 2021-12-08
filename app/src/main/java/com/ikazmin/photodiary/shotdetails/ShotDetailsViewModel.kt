package com.ikazmin.photodiary.shotdetails


import android.app.Application
import android.content.ActivityNotFoundException
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.ImageInfo
import android.graphics.ImageDecoder.OnHeaderDecodedListener
import android.icu.text.SimpleDateFormat
import android.media.Image
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ikazmin.photodiary.shotDatabase.Shot
import com.ikazmin.photodiary.shotDatabase.ShotDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URI


class ShotDetailsViewModel (
    private val database: ShotDao,application: Application, private val key: String
):AndroidViewModel(application) {

    private val _navigateToMainPage = MutableLiveData<Boolean?>()
    val navigateToMainPage: LiveData<Boolean?>
        get() = _navigateToMainPage
        val shot = MutableLiveData<Shot>()



    val textForShare=
        "Shot name: ${shot.value?.name}\n" +
                " ${shot.value?.iso}\n" +
                " ${shot.value?.diafragm}\n " +
                "${shot.value?.shutterSpeed}"



    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/*"
        putExtra(Intent.EXTRA_TEXT, textForShare)}








    fun onDeletePressed() {

        viewModelScope.launch {
            database.clear(key.toLong())
        }
        _navigateToMainPage.value = true
        dialog.isPositive.value = false
    }

    val dialog = DeleteDialog()


    val description = MutableLiveData<String>()

    fun onDescriptionAdded() {
        viewModelScope.launch {
            updateDescription()

        }
    }

    private suspend fun updateDescription() {
        withContext(Dispatchers.IO) {
            database.updateDescription(key.toLong(), description.value!!)
        }
    }


    val imageUriString = MutableLiveData<String>()

    fun onPhotoAdded() {
        viewModelScope.launch {
            updatePhoto()
        }
    }

    private suspend fun updatePhoto() {
        withContext(Dispatchers.IO) {
            database.updateImage(key.toLong(), imageUriString.value!!)
        }
    }


    init {
        initShot()
    }

    fun initShot() {
        viewModelScope.launch {
            shot.value = getShotFromDatabase()
        }
    }

    private suspend fun getShotFromDatabase(): Shot? {
        return withContext(Dispatchers.IO) {
            return@withContext database.getShotByKey(key.toLong())
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    val MyDateFormat = SimpleDateFormat("dd.MM.yyyy")

    @RequiresApi(Build.VERSION_CODES.N)
    val MyTimeFormat = SimpleDateFormat("h:mm a")





    val REQUEST_GET_PIC = 1
    val getPictureIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        type = "image/*"
    }





}































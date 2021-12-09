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
import android.graphics.ImageDecoder.*
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
    private val database: ShotDao,application: Application,
    private val key: String
):AndroidViewModel(application) {

    private val _navigateToMainPage = MutableLiveData<Boolean?>()

    val navigateToMainPage: LiveData<Boolean?>
        get() = _navigateToMainPage

    val shot = MutableLiveData<Shot>()

    val dialog = DeleteDialog()

    val description = MutableLiveData<String>()

    val imageUriString = MutableLiveData<String>()

    val textForShare=
        "Shot name: ${shot.value?.name}\n" +
                " ${shot.value?.iso}\n" +
                " ${shot.value?.diafragm}\n " +
                "${shot.value?.shutterSpeed}"


    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        type = "text/*"
        putExtra(Intent.EXTRA_TEXT, textForShare)}

    val getPictureIntent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
        type = "image/*"
    }



    fun onDeletePressed() {

        viewModelScope.launch {
            database.clear(key.toLong())
        }
        _navigateToMainPage.value = true
        dialog.isPositive.value = false
    }

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

    init {
        initShot()
    }


    val bitmap = MutableLiveData<Bitmap>()

    fun getFormattedBitmap(resolver:ContentResolver,shot:Shot){
        viewModelScope.launch {
           bitmap.value = getFormattedBitmapCoroutine(resolver,shot)
        }
    }

    private suspend fun getFormattedBitmapCoroutine(resolver:ContentResolver,shot:Shot):Bitmap{
        val source = createSource(resolver,Uri.parse(shot.imageUri))
        return withContext(Dispatchers.IO){  decodeBitmap(source){
            decoder: ImageDecoder, info: ImageInfo?, src: Source ->
            decoder.setTargetSampleSize(2)
        }
        }
    }

}































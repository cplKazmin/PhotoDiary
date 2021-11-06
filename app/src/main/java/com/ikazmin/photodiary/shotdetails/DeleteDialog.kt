package com.ikazmin.photodiary.shotdetails

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.annotation.ColorLong
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import java.lang.IllegalStateException

class DeleteDialog : DialogFragment() {

    val isPositive = MutableLiveData<Boolean>()

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Delete the shot").setMessage("Are you shure").
            setCancelable(true).
            setPositiveButton(Html.fromHtml("<font color='#B89561'>Yes</font>")){ dialog, id ->
                Toast.makeText(activity, "Deleted!",Toast.LENGTH_SHORT).show()
                isPositive.value = true
            }
                .setNegativeButton(Html.fromHtml("<font color='#B89561'>No</font>")){dialog,id->

                }

            builder.create()
        }?: throw IllegalStateException("null")

    }
}
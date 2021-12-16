package com.ikazmin.photodiary.shotdetails

import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData

class DeleteDialog : DialogFragment() {

    val isPositive = MutableLiveData<Boolean>()

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setTitle("Delete the shot").setMessage("Are you sure").
            setCancelable(true).
            setPositiveButton(Html.fromHtml("<font color='#424B80'>Yes</font>")){ dialog, id ->
                Toast.makeText(activity, "Deleted!",Toast.LENGTH_SHORT).show()
                isPositive.value = true
            }
                .setNegativeButton(Html.fromHtml("<font color='#424B80'>No</font>")){dialog,id->
                }

            builder.create()
        }?: throw IllegalStateException("null")
    }
}
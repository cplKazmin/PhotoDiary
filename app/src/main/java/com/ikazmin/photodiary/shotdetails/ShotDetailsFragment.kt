package com.ikazmin.photodiary.shotdetails

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.createSource
import android.graphics.ImageDecoder.decodeBitmap
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_HOME
import androidx.appcompat.app.ActionBar.DISPLAY_USE_LOGO
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ikazmin.photodiary.R
import com.ikazmin.photodiary.Utils
import com.ikazmin.photodiary.databinding.ShotDetailsFragmentBinding
import com.ikazmin.photodiary.shotDatabase.Shot
import com.ikazmin.photodiary.shotDatabase.ShotDatabase
import kotlinx.android.synthetic.main.fragment_main_page.*
import kotlinx.android.synthetic.main.shot_details_fragment.*

class ShotDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, SavedInstanceState: Bundle?): View?{

        val binding: ShotDetailsFragmentBinding = DataBindingUtil.inflate(
            inflater,R.layout.shot_details_fragment,container,false)

        val key = arguments?.getString("key")

        val application = requireNotNull(this.activity).application

        val dataSource = ShotDatabase.getInstance(application).shotDao

        val viewModelFactory = ShotDetailsViewModelFactory(dataSource,application,key!!)

        val shotDetailsViewModel = ViewModelProvider(
            this,viewModelFactory).get(ShotDetailsViewModel::class.java)


        val shotObserver = Observer<Shot> { newShot ->
            binding.shotName.text = newShot.name
            binding.shotIso.text = newShot.iso
            binding.shotDiafragm.text = newShot.diafragm
            binding.shotShutterspeed.text = newShot.shutterSpeed
            binding.shotDate.text = Utils.myDateFormat.format(newShot.date)
            binding.shotTime.text = Utils.myTimeFormat.format(newShot.date)
            binding.shotDescriptionEdittext.setText(newShot.shortDescription)
            try {
                if (newShot.imageUri.isNotEmpty()) {
                    val resolver = activity?.contentResolver
                    resolver!!.takePersistableUriPermission(
                        Uri.parse(newShot.imageUri),
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    shotDetailsViewModel.getFormattedBitmap(resolver,newShot)
                }
            } catch (e:SecurityException) {
                Toast.makeText(activity?.applicationContext,"no image found",Toast.LENGTH_SHORT).show()
            }
        }

        val dialogObserver = Observer<Boolean> {
            if (it == true){
                 shotDetailsViewModel.navigateToMainPage.value
                 shotDetailsViewModel.onDeletePressed()}
            }


        val navigateToMainPageObserver = Observer<Boolean?> {
            if (it == true){
              findNavController().navigate(R.id.action_shotDetailsFragment_to_mainPageFragment)
            }
        }


        setHasOptionsMenu(true)
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.subtitle = "shot info";
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)



        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK){
                val  fullPhoto: Uri? = result.data?.data
                shotDetailsViewModel.imageUriString.value = fullPhoto.toString()
            }
            shotDetailsViewModel.onPhotoAdded()
            shotDetailsViewModel.initShot()
        }


        shotDetailsViewModel.bitmap.observe(viewLifecycleOwner, {
            binding.imageView3.setImageBitmap(it)
        })

        shotDetailsViewModel.dialog.isPositive.observe(viewLifecycleOwner,dialogObserver)

        shotDetailsViewModel.navigateToMainPage.observe(viewLifecycleOwner,navigateToMainPageObserver)

        shotDetailsViewModel.shot.observe(viewLifecycleOwner,shotObserver)


        binding.deleteButton.setOnClickListener{
        shotDetailsViewModel.dialog.show(childFragmentManager,"dialog")
        }

        binding.imageView3.setOnClickListener{
            startForResult.launch(shotDetailsViewModel.getPictureIntent)
        }

        binding.shotDescriptionEdittext.addTextChangedListener{
            shotDetailsViewModel.description.value = binding.shotDescriptionEdittext.text.toString()
            shotDetailsViewModel.onDescriptionAdded()
        }


        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home){
            findNavController().navigate(R.id.action_shotDetailsFragment_to_mainPageFragment)
            true
        } else false
    }


}

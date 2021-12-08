package com.ikazmin.photodiary.shotdetails

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.ImageDecoder.createSource
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ikazmin.photodiary.R
import com.ikazmin.photodiary.databinding.ShotDetailsFragmentBinding
import com.ikazmin.photodiary.shotDatabase.Shot
import com.ikazmin.photodiary.shotDatabase.ShotDatabase
import kotlinx.android.synthetic.main.fragment_main_page.*
import kotlinx.android.synthetic.main.shot_details_fragment.*

class ShotDetailsFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.P)
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
            binding.shotDate.text = shotDetailsViewModel.MyDateFormat.format(newShot.date)
            binding.shotTime.text = shotDetailsViewModel.MyTimeFormat.format(newShot.date)
            binding.shotDescriptionEdittext.setText(newShot.shortDescription)
            try {
                if (newShot.imageUri.isNotEmpty()) {
                    val resolver = activity?.contentResolver
                    resolver!!.takePersistableUriPermission(
                        Uri.parse(newShot.imageUri),
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                    val source = createSource(resolver, Uri.parse(newShot.imageUri))
                    val bitmap: Bitmap =
                        ImageDecoder.decodeBitmap(source) {
                                decoder: ImageDecoder, info: ImageDecoder.ImageInfo?,
                                src: ImageDecoder.Source? -> decoder.setTargetSampleSize(2)
                        }
                    binding.imageView3.setImageBitmap(bitmap)
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


        val toolbar = binding.myToolbar
            toolbar.setupWithNavController(findNavController())
            toolbar.setNavigationIcon(R.drawable.back_button)
            toolbar.inflateMenu(R.menu.my_menu)
            binding.myToolbar.setTitleTextColor(resources.getColor(R.color.blue))
            toolbar.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener {
                 startActivity(Intent.createChooser(shotDetailsViewModel.sendIntent,null))
                 return@OnMenuItemClickListener true
        })


        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                result: ActivityResult ->
            if (result.resultCode == RESULT_OK){
                val  fullPhoto: Uri? = result.data?.data
                shotDetailsViewModel.imageUriString.value = fullPhoto.toString()
            }
            shotDetailsViewModel.onPhotoAdded()
            shotDetailsViewModel.initShot()
        }



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
}

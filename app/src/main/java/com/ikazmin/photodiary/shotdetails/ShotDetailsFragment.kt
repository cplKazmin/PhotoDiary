package com.ikazmin.photodiary.shotdetails

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ikazmin.photodiary.R
import com.ikazmin.photodiary.Utils
import com.ikazmin.photodiary.databinding.ShotDetailsFragmentBinding
import com.ikazmin.photodiary.shotDatabase.Shot
import com.ikazmin.photodiary.shotDatabase.ShotDatabase

class ShotDetailsFragment : Fragment() {

    //vm
    private val shotDetailsViewModel: ShotDetailsViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val key = arguments?.getString("key")
        val dataSource = ShotDatabase.getInstance(application).shotDao
        val viewModelFactory = ShotDetailsViewModelFactory(
            dataSource,application,key!!)
        ViewModelProvider(this, viewModelFactory).get(ShotDetailsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, SavedInstanceState: Bundle?): View {

        //binding
        val binding: ShotDetailsFragmentBinding = DataBindingUtil.inflate(
            inflater,R.layout.shot_details_fragment,container,false)

    //observers
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

    //appbar
        setHasOptionsMenu(true)
        val actionBar = (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.subtitle = "shot info"
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
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().navigate(R.id.action_shotDetailsFragment_to_mainPageFragment)
            }
            R.id.shareButton -> {
                startActivity(shotDetailsViewModel.getIntent())
            }
        }
        return true
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.my_menu, menu)

    }
}

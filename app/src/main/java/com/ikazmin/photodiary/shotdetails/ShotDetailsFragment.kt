package com.ikazmin.photodiary.shotdetails

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.ikazmin.photodiary.R
import com.ikazmin.photodiary.databinding.ShotDetailsFragmentBinding
import com.ikazmin.photodiary.shotDatabase.Shot
import com.ikazmin.photodiary.shotDatabase.ShotDatabase
import kotlinx.android.synthetic.main.fragment_main_page.*

class ShotDetailsFragment : Fragment() {

//    var shot:Shot= Shot()

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


        shotDetailsViewModel.dialog.isPositive.observe(viewLifecycleOwner, Observer {
            if (it == true){
            shotDetailsViewModel.navigateToMainPage.value
            shotDetailsViewModel.onDeletePressed()}
        })

       val shotObserver = Observer<Shot> { newShot ->
           binding.shotName.text = newShot.name
           binding.shotIso.text = newShot.iso
           binding.shotDiafragm.text = newShot.diafragm
           binding.shotShutterspeed.text = newShot.shutterSpeed
           binding.shotDate.text = shotDetailsViewModel.MyDateFormat.format(newShot.date)
           binding.shotTime.text = shotDetailsViewModel.MyTimeFormat.format(newShot.date)
           binding.shotDescriptionEdittext.setText(newShot.shortDescription)
          // shot = shotDetailsViewModel.shot.value!!
       }


        shotDetailsViewModel.shot.observe(viewLifecycleOwner,shotObserver)

        binding.deleteButton.setOnClickListener{
            shotDetailsViewModel.dialog.show(childFragmentManager,"kaz")
        }
        shotDetailsViewModel.navigateToMainPage.observe(viewLifecycleOwner, Observer {
            if (it == true){
                findNavController().navigate(R.id.action_shotDetailsFragment_to_mainPageFragment)
            }
        })

        binding.shotDescriptionEdittext.addTextChangedListener{
            shotDetailsViewModel.description.value = binding.shotDescriptionEdittext.text.toString()
            shotDetailsViewModel.onDescriptionAdded()
        }
        return binding.root
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when(item!!.itemId){
//            R.id.share -> startActivity(getShareIntent())
//        }
//        return super.onOptionsItemSelected(item)
//    }
//
//
//    fun getShareIntent(): Intent {
//        val args =  "Hello."
//        val shareIntent = Intent(Intent.ACTION_SEND)
//        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, args)
//        return shareIntent
//    }


}



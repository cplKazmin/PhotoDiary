package com.ikazmin.photodiary.newFilm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.toColorInt
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.ikazmin.photodiary.R
import com.ikazmin.photodiary.databinding.FragmentNewshotBinding

import com.ikazmin.photodiary.shotDatabase.ShotDatabase

class NewShotFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //Байндинг
        val binding: FragmentNewshotBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_newshot,container,false
        )

        setHasOptionsMenu(true)
        val actionBar =   (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.subtitle = "new shot";
        actionBar?.setHomeButtonEnabled(true)
        actionBar?.setDisplayHomeAsUpEnabled(true)





        //БД
        val application = requireNotNull(this.activity).application
        val dataSource = ShotDatabase.getInstance(application).shotDao

        //ViewModel
        val viewModelFactory = NewShotViewModelFactory(dataSource,application)
        val newFilmFragmentViewModel =
            ViewModelProvider(
                this,viewModelFactory).get(NewShotFragmentViewModel::class.java)

        //обработка нажатия сохран.
        binding.saveButton.setOnClickListener {
            val iso = binding.isoText.text.toString()
            val name = binding.filmNameText.text.toString()
            val diafragm = binding.diafragmText.text.toString()
            val shutterSpeed =  binding.shutterSpeedText.text.toString()

            if (newFilmFragmentViewModel.isFilled(name,iso,shutterSpeed,diafragm)){
            newFilmFragmentViewModel.onSaveButtonPressed(
                iso = "iso:$iso",
                name = name,
                diafragm = "ap:$diafragm",
                shutterSpeed = "sh/s:$shutterSpeed",
            )}
            else
                Toast.makeText(activity?.applicationContext,"Please, complete the information",Toast.LENGTH_SHORT).show()
        }




        //навигация
        newFilmFragmentViewModel.navigateToMainPage.observe(viewLifecycleOwner, Observer {
            if (it==true ){
                this.findNavController().navigate(R.id.action_newShotFragment_to_mainPageFragment)
                newFilmFragmentViewModel.doneNavigating()
            }

        })

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home){
            findNavController().navigate(R.id.action_newShotFragment_to_mainPageFragment)
            true
        } else false
    }


}
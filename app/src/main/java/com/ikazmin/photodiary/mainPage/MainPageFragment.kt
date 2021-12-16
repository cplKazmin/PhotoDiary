package com.ikazmin.photodiary.mainPage


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.ikazmin.photodiary.R
import com.ikazmin.photodiary.databinding.FragmentMainPageBinding
import com.ikazmin.photodiary.shotDatabase.ShotDatabase


class MainPageFragment : Fragment(),ShotOnClickListener {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        //Binding
        val binding: FragmentMainPageBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_main_page,container,false
        )


        setHasOptionsMenu(true)
        val actionBar =   (activity as AppCompatActivity?)!!.supportActionBar
        actionBar?.subtitle = "shots"
        actionBar?.setHomeButtonEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(false)


        //Работа с БД
        val application = requireNotNull(this.activity).application
        val dataSource = ShotDatabase.getInstance(application).shotDao
        //ViewModel
        val viewModelFactory = MainPageViewModelFactory(dataSource,application)
        val mainPageViewModel =ViewModelProvider(
                this,viewModelFactory).get(MainPageViewModel::class.java)

        //Работа с Ресайклером
        val adapter = ShotAdapter(this)
        binding.shotList.adapter = adapter
        mainPageViewModel.shots.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })


        //Навигация на фрагмент добавления снимка
        binding.button.setOnClickListener(
            Navigation.createNavigateOnClickListener(R.id.action_mainPageFragment_to_newShotFragment)
        )
        return binding.root
    }

    //Обработка нажатия на элемент ресайклера
    override fun onShotItemClick(position: Int, key : String ) {
        val bundle = bundleOf("key" to key)
              findNavController().navigate(R.id.action_mainPageFragment_to_shotDetailsFragment,bundle)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home){
            findNavController().navigate(R.id.action_mainPageFragment_to_newShotFragment)
            return true}
        else return false
    }


}

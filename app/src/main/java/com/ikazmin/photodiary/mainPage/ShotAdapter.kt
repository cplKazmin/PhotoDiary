package com.ikazmin.photodiary.mainPage

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ikazmin.photodiary.R
import com.ikazmin.photodiary.shotDatabase.Shot

class ShotAdapter (private val shotOnClickListener: ShotOnClickListener)  : RecyclerView.Adapter<ShotAdapter.ViewHolder>() {
    //БД
    var data = listOf<Shot>()
    set(value){
        field =  value
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            shotOnClickListener.onShotItemClick(position,data[position].shotId.toString())
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_shot, parent, false)

        return ViewHolder(view)

    }


    inner class ViewHolder (ItemView: View) : RecyclerView.ViewHolder(ItemView){

        val shotName: TextView = itemView.findViewById(R.id.shot_name)
        val shotIso: TextView = itemView.findViewById(R.id.shot_iso)
        val shotDiafragm: TextView = itemView.findViewById(R.id.shot_diafragm)
        val shotShutterSpeed: TextView = itemView.findViewById(R.id.shot_shutterspeed)
        val shotDate: TextView = itemView.findViewById(R.id.shot_date)
        val shotTime: TextView = itemView.findViewById(R.id.shot_time)

        @RequiresApi(Build.VERSION_CODES.N)
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")

        @RequiresApi(Build.VERSION_CODES.N)
        val simpleTimeFormat = SimpleDateFormat("h:mm a")

        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(
            item: Shot
        ) {
            shotName.text = item.name
            shotIso.text = item.iso
            shotDiafragm.text = item.diafragm
            shotShutterSpeed.text = item.shutterSpeed
            shotDate.text = simpleDateFormat.format(item.date)
            shotTime.text = simpleTimeFormat.format(item.date)
        }


        }
    }






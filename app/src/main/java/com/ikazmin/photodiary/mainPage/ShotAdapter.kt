package com.ikazmin.photodiary.mainPage

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ikazmin.photodiary.R
import com.ikazmin.photodiary.Utils.myDateFormat
import com.ikazmin.photodiary.Utils.myTimeFormat
import com.ikazmin.photodiary.shotDatabase.Shot

class ShotAdapter (private val shotOnClickListener: ShotOnClickListener)  : ListAdapter<Shot,ShotAdapter.ViewHolder>(ShotDiffCallback()) {


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.itemView.setOnClickListener {
            shotOnClickListener.onShotItemClick(position,getItem(position).shotId.toString())
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.list_item_shot, parent, false)

        return ViewHolder(view)

    }


    inner class ViewHolder (ItemView: View) : RecyclerView.ViewHolder(ItemView){

         private val shotName: TextView = itemView.findViewById(R.id.shot_name)
         private val shotIso: TextView = itemView.findViewById(R.id.shot_iso)
         private val shotDiafragm: TextView = itemView.findViewById(R.id.shot_diafragm)
         private val shotShutterSpeed: TextView = itemView.findViewById(R.id.shot_shutterspeed)
         private val shotDate: TextView = itemView.findViewById(R.id.shot_date)
         private val shotTime: TextView = itemView.findViewById(R.id.shot_time)



        fun bind(
            item: Shot
        ) {
            shotName.text = item.name
            shotIso.text = item.iso
            shotDiafragm.text = item.diafragm
            shotShutterSpeed.text = item.shutterSpeed
            shotDate.text = myDateFormat.format(item.date)
            shotTime.text = myTimeFormat.format(item.date)
        }
        }
    }

class ShotDiffCallback : DiffUtil.ItemCallback<Shot>(){
    override fun areItemsTheSame(oldItem: Shot, newItem: Shot): Boolean {
        return oldItem.shotId == newItem.shotId
    }

    override fun areContentsTheSame(oldItem: Shot, newItem: Shot): Boolean {
        return oldItem == newItem
    }
}



